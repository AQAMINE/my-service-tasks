# my-service-tasks

Microservice Spring Boot — **gestionnaire de tâches / Kanban**.  
Fait partie de la plateforme MyService (auth Keycloak, PostgreSQL partagé via infra Docker).

> Document de référence (humain + LLM) : structure, ports, APIs, DB, sécurité.

---

## Rôle dans le workspace

```
Documents/GitHub/
├── my-service/            # passwords / accounts API (:8081)
├── my-service-cards/      # bank cards API (:8083)
├── my-service-tasks/      # ← ce service (:8084)
├── my-service-crypto/     # gRPC AES-GCM (non utilisé par tasks)
├── my-service-ui/         # Angular SPA (:4200)
└── my-service-infra/      # Docker Compose (Postgres, Keycloak, …)
```

| Dépendance | Rôle |
|------------|------|
| PostgreSQL | DB `my_tasks_db` (host **5435**) |
| Keycloak | JWT realm `my-service-realm` (host **8085**) |
| UI Angular | CORS `http://localhost:4200` |

---

## Tech stack

| Item | Version / choix |
|------|-----------------|
| Java | **21** |
| Spring Boot | **4.1.1** |
| Build | Maven (`pom.xml`, `mvnw`) |
| Web | `spring-boot-starter-webmvc` |
| Security | OAuth2 Resource Server (JWT Keycloak) |
| Persistence | Spring Data JPA + PostgreSQL + Flyway |
| Other | Lombok, Validation |

---

## Port & config

| Contexte | Valeur |
|----------|--------|
| App port | **8084** |
| Datasource (local) | `jdbc:postgresql://localhost:5435/my_tasks_db` |
| DB user / password | `postgres` / `postgrespassword` |
| JWT issuer | `http://localhost:8085/realms/my-service-realm` |
| Keycloak client-id | `my-service-tasks` |
| Docker host map | **8084:8084** (`my-service-infra`) |

Fichier : `src/main/resources/application.yaml`

> La DB `my_tasks_db` n’est **pas** créée automatiquement par Postgres (`POSTGRES_DB=myservicedb`). Il faut la créer manuellement si absente :
> ```sql
> CREATE DATABASE my_tasks_db;
> ```

---

## Architecture hexagonale

```
com.myservicetasks.my_service_tasks
├── MyServiceTasksApplication.java
├── domain/
│   ├── model/                 # Task, TaskStatus, TaskPriority
│   └── ports/
│       ├── in/TaskUseCase     # driving port
│       └── out/TaskRepositoryPort
├── application/
│   └── service/TaskService    # implémentation use cases
└── infrastructure/
    ├── adapter/in/web/        # TaskController, DTOs
    ├── adapter/out/persistence/
    │   ├── entity/            # TaskEntity, TaskStatusEntity, TaskPriorityEntity
    │   ├── repository/        # Spring Data JPA
    │   ├── mapper/
    │   └── TaskPersistenceAdapter
    └── config/security/       # SecurityConfig, KeycloakJwtAuthenticationConverter
```

---

## Structure des dossiers

```
my-service-tasks/
├── Dockerfile
├── pom.xml
├── mvnw / mvnw.cmd
├── README.md                  # ce fichier
└── src/main/
    ├── java/com/myservicetasks/my_service_tasks/
    └── resources/
        ├── application.yaml
        └── db/migration/
            ├── V1__init_tasks_schema.sql
            └── V2__insert_default_task_metadata.sql
```

---

## API REST

Base path : `/api/v1/tasks`  
Auth : **Bearer JWT** (Keycloak). User id = claim JWT `sub` (UUID).

| Method | Path | Auth | Description |
|--------|------|------|-------------|
| `GET` | `/api/v1/tasks` | JWT | Liste des tâches de l’utilisateur |
| `GET` | `/api/v1/tasks/statuses` | JWT | Statuts Kanban (système) |
| `GET` | `/api/v1/tasks/priorities` | JWT | Priorités (système) |
| `POST` | `/api/v1/tasks` | JWT | Créer une tâche |
| `DELETE` | `/api/v1/tasks/{id}` | JWT | Supprimer (si appartient à l’user) |

Public : `/actuator/**` uniquement.  
CORS : `http://localhost:4200` (credentials autorisés).

### Body `POST /api/v1/tasks` (`CreateTaskRequest`)

```json
{
  "title": "Ma tâche",
  "description": "optionnel",
  "statusId": "<uuid>",
  "priorityId": "<uuid>",
  "position": 0,
  "dueDate": "2026-10-20T12:00:00Z"
}
```

Validation : `title` obligatoire, `statusId` / `priorityId` obligatoires.

---

## Base de données (`my_tasks_db`)

### Tables (V1)

| Table | Rôle |
|-------|------|
| `task_statuses` | Colonnes Kanban (`code`, `label`, `color`, `position`, `is_system`) |
| `task_priorities` | Priorités (`code`, `label`, `color`, `level`, `is_system`) |
| `tasks` | Tâches user (`user_id`, `title`, `description`, FK status/priority, `position`, `due_date`, timestamps) |

Index : `idx_tasks_user_status` sur `(user_id, status_id)`.

### Seed (V2)

**Statuts :** `TODO`, `IN_PROGRESS`, `IN_REVIEW`, `DONE`  
**Priorités :** `LOW`, `MEDIUM`, `HIGH`, `URGENT`

---

## Sécurité

- Stateless JWT resource server.
- Roles Keycloak realm → `ROLE_*` via `KeycloakJwtAuthenticationConverter` (`realm_access.roles`).
- Pas de header `X-User-Id` : identité uniquement via JWT `sub`.
- Client Keycloak attendu : `my-service-tasks` (secret placeholder dans yaml : `votre-client-secret` — à aligner avec le realm).

---

## Docker

```dockerfile
# Multi-stage : Maven 21 build → JRE 21 Alpine
EXPOSE 8084
```

Compose (`my-service-infra`) :

```yaml
my-service-tasks:
  ports: ["8084:8084"]
  SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/my_tasks_db
  JWT issuer: http://keycloak:8080/realms/my-service-realm
```

---

## Démarrage local

1. Infra déjà up (Postgres **5435**, Keycloak **8085**) :
   ```bash
   cd ../my-service-infra
   docker compose up -d postgres keycloak
   ```
2. Créer la DB si besoin : `CREATE DATABASE my_tasks_db;`
3. Lancer le service :
   ```bash
   cd /Users/amineaqeblip/Documents/GitHub/my-service-tasks
   ./mvnw spring-boot:run
   ```
4. API : **http://localhost:8084**

---

## Concepts domaine

- **Task** : appartenance stricte à un `userId` (isolation multi-tenant soft).
- **TaskStatus / TaskPriority** : référentiels système seedés (`is_system = true`).
- **position** : ordre dans une colonne Kanban.
- **dueDate** : échéance optionnelle (`Instant` / `TIMESTAMPTZ`).

---

## Notes / points d’attention

1. Client Keycloak `my-service-tasks` + secret doivent exister dans le realm export.
2. DB `my_tasks_db` à créer manuellement (Postgres init ne crée que `myservicedb`).
3. Pas d’appel gRPC crypto (contrairement à `my-service` / `my-service-cards`).
4. Pas encore d’endpoints update / move / reorder dans le controller actuel (CRUD partiel : list, create, delete + metadata).
