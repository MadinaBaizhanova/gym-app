CREATE SEQUENCE IF NOT EXISTS user_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS trainee_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS trainer_sequence START WITH 1 INCREMENT BY 1;
CREATE SEQUENCE IF NOT EXISTS training_sequence START WITH 1 INCREMENT BY 1;

CREATE TABLE IF NOT EXISTS training_type
(
    id                 BIGINT AUTO_INCREMENT PRIMARY KEY,
    training_type_name VARCHAR(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(128) NOT NULL,
    last_name  VARCHAR(128) NOT NULL,
    username   VARCHAR(256) NOT NULL UNIQUE,
    password   VARCHAR(128) NOT NULL,
    is_active  BOOLEAN      NOT NULL
);

CREATE TABLE IF NOT EXISTS trainee
(
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id       BIGINT NOT NULL,
    date_of_birth DATE,
    address       VARCHAR(256),
    CONSTRAINT fk_user_trainee FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS trainer
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id          BIGINT NOT NULL,
    training_type_id BIGINT NOT NULL,
    CONSTRAINT fk_user_trainer FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_training_type_trainer FOREIGN KEY (training_type_id) REFERENCES training_type (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS trainee_trainer
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainee_id BIGINT NOT NULL,
    trainer_id BIGINT NOT NULL,
    CONSTRAINT fk_trainee FOREIGN KEY (trainee_id) REFERENCES trainee (id) ON DELETE CASCADE,
    CONSTRAINT fk_trainer FOREIGN KEY (trainer_id) REFERENCES trainer (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS training
(
    id                BIGINT AUTO_INCREMENT PRIMARY KEY,
    trainee_id        BIGINT       NOT NULL,
    trainer_id        BIGINT       NOT NULL,
    training_name     VARCHAR(128) NOT NULL,
    training_type_id  BIGINT       NOT NULL,
    training_date     DATE         NOT NULL,
    training_duration INT          NOT NULL,
    CONSTRAINT fk_trainee_training FOREIGN KEY (trainee_id) REFERENCES trainee (id) ON DELETE CASCADE,
    CONSTRAINT fk_trainer_training FOREIGN KEY (trainer_id) REFERENCES trainer (id) ON DELETE CASCADE,
    CONSTRAINT fk_training_type_training FOREIGN KEY (training_type_id) REFERENCES training_type (id) ON DELETE CASCADE
);