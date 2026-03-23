-- Kopiujemy statystyki użytkownika testowego do wszystkich innych użytkowników, aby wykresy działały na każdym koncie
INSERT INTO statistics (score, completion_date, user_id)
SELECT s.score, s.completion_date, u.id
FROM statistics s
CROSS JOIN users u
WHERE s.user_id = (SELECT id FROM users WHERE username = 'userTest' LIMIT 1)
  AND u.username != 'userTest';
