CREATE TABLE IF NOT EXISTS users
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(128) NOT NULL,
    last_name  VARCHAR(128) NOT NULL,
    username   VARCHAR(256) NOT NULL UNIQUE,
    password   VARCHAR(10)  NOT NULL,
    is_active  BOOLEAN      NOT NULL
);

CREATE TABLE IF NOT EXISTS training_type
(
    id                 SERIAL PRIMARY KEY,
    training_type_name VARCHAR(128) NOT NULL
);

CREATE TABLE IF NOT EXISTS trainee
(
    id            SERIAL PRIMARY KEY,
    user_id       INT NOT NULL,
    date_of_birth DATE,
    address       VARCHAR(256),
    CONSTRAINT fk_user_trainee FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS trainer
(
    id               SERIAL PRIMARY KEY,
    user_id          INT NOT NULL,
    training_type_id INT NOT NULL,
    CONSTRAINT fk_user_trainer FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_training_type_trainer FOREIGN KEY (training_type_id) REFERENCES training_type (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS trainee_trainer
(
    id         SERIAL PRIMARY KEY,
    trainee_id INT NOT NULL,
    trainer_id INT NOT NULL,
    CONSTRAINT fk_trainee FOREIGN KEY (trainee_id) REFERENCES trainee (id) ON DELETE CASCADE,
    CONSTRAINT fk_trainer FOREIGN KEY (trainer_id) REFERENCES trainer (id) ON DELETE CASCADE
);

CREATE TABLE training
(
    id                SERIAL PRIMARY KEY,
    trainee_id        INT          NOT NULL,
    trainer_id        INT          NOT NULL,
    training_name     VARCHAR(128) NOT NULL,
    training_type_id  INT          NOT NULL,
    training_date     DATE         NOT NULL,
    training_duration INT          NOT NULL,
    CONSTRAINT fk_trainee_training FOREIGN KEY (trainee_id) REFERENCES trainee (id) ON DELETE CASCADE,
    CONSTRAINT fk_trainer_training FOREIGN KEY (trainer_id) REFERENCES trainer (id) ON DELETE CASCADE,
    CONSTRAINT fk_training_type_training FOREIGN KEY (training_type_id) REFERENCES training_type (id) ON DELETE CASCADE
);