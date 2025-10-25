CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name  VARCHAR(255)        NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    annotation         VARCHAR(2000)               NOT NULL,
    category_id        BIGINT                      NOT NULL REFERENCES categories (id),
    description        VARCHAR(7000)               NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    lat                FLOAT8                      NOT NULL,
    lon                FLOAT8                      NOT NULL,
    paid               BOOLEAN DEFAULT FALSE       NOT NULL,
    participant_limit  INTEGER DEFAULT 0           NOT NULL,
    request_moderation BOOLEAN DEFAULT TRUE        NOT NULL,
    event_title        VARCHAR(120)                NOT NULL,
    created_on         TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    initiator_id       BIGINT                      NOT NULL REFERENCES users (id),
    published_on       TIMESTAMP,
    event_state        VARCHAR(20)                 NOT NULL DEFAULT 'PENDING',
    CONSTRAINT event_valid_state CHECK (event_state IN ('PENDING', 'PUBLISHED', 'CANCELED'))
);

CREATE TABLE IF NOT EXISTS events_requests
(
    id           BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id     BIGINT                      NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    status       VARCHAR(20)                 NOT NULL DEFAULT 'PENDING',
    requester_id BIGINT                      NOT NULL REFERENCES users (id),
    created_on   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT request_valid_status CHECK (status IN ('PENDING', 'CONFIRMED', 'CANCELED', 'REJECTED'))
);

CREATE TABLE IF NOT EXISTS events_views
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id BIGINT      NOT NULL REFERENCES events (id) ON DELETE CASCADE,
    ip       VARCHAR(45) NOT NULL,
    CONSTRAINT uq_event_ip UNIQUE (event_id, ip)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id       BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    pinned BOOLEAN NOT NULL,
    title VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS events_compilations
(
    event_id BIGINT NOT NULL REFERENCES events(id) ON DELETE CASCADE,
    compilation_id BIGINT NOT NULL REFERENCES compilations(id) ON DELETE CASCADE,
    PRIMARY KEY (event_id, compilation_id)
);