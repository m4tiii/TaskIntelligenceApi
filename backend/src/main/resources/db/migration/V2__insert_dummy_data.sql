-- Dodajemy tylko i wyłącznie JEDNEGO użytkownika do testów. Login: userTest, Hasło: password
INSERT INTO users (username, email, first_name, last_name, country, password, role)
VALUES (
    'userTest',
    'usertest@gmail.com',
    'Główny',
    'Tester',
    'Poland',
    '$2a$10$8.UnVuG9HLpUsXVTqe8B9.D0cfR9n9yE7D7A2Zf.v5W1b.m/B3cT6',
    'ROLE_USER'
) ON CONFLICT (username) DO NOTHING;

-- Generujemy równe 10 000 zadań przypisanych do Ciebie
INSERT INTO tasks (title, description, created_at, deadline_to, importance, task_status, priority_score, user_id)
SELECT 
    'Masowe Zadanie Testowe ' || i,
    'Zadanie wygenerowane na poczet testów wydajnościowych API dla użytkownika. Numer zadania: ' || i,
    CURRENT_TIMESTAMP - (random() * 30 || ' days')::interval,
    CURRENT_TIMESTAMP + (random() * 30 || ' days')::interval,
    floor(random() * 10 + 1)::INT,
    CASE 
        WHEN random() < 0.50 THEN 'COMPLETED' 
        WHEN random() < 0.75 THEN 'NEW' 
        ELSE 'IN_PROGRESS' 
    END,
    random() * 100,
    (SELECT id FROM users WHERE username = 'userTest')
FROM generate_series(1, 10000) i;

-- Generowanie wpisów do statystyk dla w/w 5000 zadań o statusie COMPLETED
INSERT INTO statistics (score, completion_date, user_id)
SELECT
    priority_score, 
    CURRENT_DATE - (floor(random() * 30)::INT),
    user_id
FROM tasks
WHERE task_status = 'COMPLETED';
