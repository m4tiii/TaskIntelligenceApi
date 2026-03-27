
CREATE TABLE notifications(
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message TEXT NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    type VARCHAR(255) NOT NULL,
    task_id BIGINT NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_notification_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);