CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50),
    refresh_token VARCHAR(1000) UNIQUE,
    refresh_token_expiration TIMESTAMP
);

CREATE TABLE tasks (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    created_at TIMESTAMP,
    deadline_to TIMESTAMP NOT NULL,
    user_id BIGINT,
    importance INTEGER NOT NULL,
    task_status VARCHAR(50),
    priority_score DOUBLE PRECISION NOT NULL,
    CONSTRAINT fk_tasks_user_id FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE statistics (
    id BIGSERIAL PRIMARY KEY,
    score DOUBLE PRECISION NOT NULL,
    completion_date DATE NOT NULL,
    user_id BIGINT,
    CONSTRAINT fk_statistics_user_id FOREIGN KEY (user_id) REFERENCES users(id)
);