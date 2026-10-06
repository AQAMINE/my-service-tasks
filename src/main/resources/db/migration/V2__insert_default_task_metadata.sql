-- Statuts par défaut
INSERT INTO task_statuses (id, code, label, color, position, is_system) VALUES
(gen_random_uuid(), 'TODO', 'À faire', '#6C757D', 1, true),
(gen_random_uuid(), 'IN_PROGRESS', 'En cours', '#007BFF', 2, true),
(gen_random_uuid(), 'IN_REVIEW', 'En révision', '#FFC107', 3, true),
(gen_random_uuid(), 'DONE', 'Terminé', '#28A745', 4, true);

-- Priorités par défaut
INSERT INTO task_priorities (id, code, label, color, level, is_system) VALUES
(gen_random_uuid(), 'LOW', 'Basse', '#6C757D', 1, true),
(gen_random_uuid(), 'MEDIUM', 'Moyenne', '#17A2B8', 2, true),
(gen_random_uuid(), 'HIGH', 'Haute', '#FD7E14', 3, true),
(gen_random_uuid(), 'URGENT', 'Urgente', '#DC3545', 4, true);