-- Dodajemy tylko i wyłącznie JEDNEGO użytkownika do testów. Login: userTest, Hasło: password
INSERT INTO users (username, email, first_name, last_name, country, password, role)
VALUES (
           'userTest',
           'usertest@gmail.com',
           'Główny',
           'Tester',
           'Poland',
           '$2a$10$o89sUIuSQfm9RhA7ZJMH1.r2/roOmHzVJ1rbFH88WQC59eTsquz2G',
           'USER'
       ) ON CONFLICT (username) DO NOTHING;

-- Generujemy równe 10 000 zadań przypisanych do Ciebie
INSERT INTO tasks (title, description, created_at, deadline, importance, task_status, priority_score, user_id)
SELECT
    'Masowe Zadanie Testowe ' || i,
    'Zadanie wygenerowane na poczet testów wydajnościowych API dla użytkownika. Numer zadania: ' || i,
    TIMESTAMPTZ '2026-03-01T00:00:00Z' +
    random() * (TIMESTAMPTZ '2026-03-25T23:59:59Z' - TIMESTAMPTZ '2026-03-01T00:00:00Z'),
    TIMESTAMPTZ '2026-03-01T00:00:00Z' +
    random() * (TIMESTAMPTZ '2026-03-30T23:59:59Z' - TIMESTAMPTZ '2026-03-01T00:00:00Z'),
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
SELECT priority_score,
       CURRENT_TIMESTAMP - (random() * interval '30 days'),
    user_id
FROM tasks
WHERE task_status = 'COMPLETED';
