CREATE TABLE roles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    phone_number VARCHAR(50),
    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT chk_users_status
        CHECK (status IN ('ACTIVE', 'INACTIVE'))
);

CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,

    PRIMARY KEY (user_id, role_id),

    CONSTRAINT fk_user_roles_user
        FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_roles_role
        FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE responder_availability (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    responder_id BIGINT NOT NULL,

    available_from DATETIME NOT NULL,
    available_until DATETIME NOT NULL,
    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_availability_responder
        FOREIGN KEY (responder_id) REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT chk_availability_status
        CHECK (status IN ('AVAILABLE', 'UNAVAILABLE')),

    CONSTRAINT chk_availability_time_order
        CHECK (available_from < available_until)
);

CREATE TABLE check_in_methods (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE check_in_sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    customer_id BIGINT NOT NULL,
    responder_id BIGINT NOT NULL,

    start_at DATETIME NOT NULL,
    end_at DATETIME NOT NULL,
    latest_check_in_at DATETIME NOT NULL,

    location_description TEXT NOT NULL,
    important_notes TEXT,

    status VARCHAR(30) NOT NULL,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_sessions_customer
        FOREIGN KEY (customer_id) REFERENCES users(id),

    CONSTRAINT fk_sessions_responder
        FOREIGN KEY (responder_id) REFERENCES users(id),

    CONSTRAINT chk_sessions_status
        CHECK (status IN (
            'PLANNED',
            'ACTIVE',
            'CHECKED_IN',
            'MISSED',
            'ESCALATED',
            'CANCELLED'
        )),

    CONSTRAINT chk_sessions_time_order
        CHECK (start_at < end_at),

    CONSTRAINT chk_sessions_latest_check_in
        CHECK (end_at <= latest_check_in_at)
);

CREATE TABLE session_check_in_methods (
    session_id BIGINT NOT NULL,
    method_id BIGINT NOT NULL,

    PRIMARY KEY (session_id, method_id),

    CONSTRAINT fk_session_methods_session
        FOREIGN KEY (session_id) REFERENCES check_in_sessions(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_session_methods_method
        FOREIGN KEY (method_id) REFERENCES check_in_methods(id)
);

CREATE TABLE session_events (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    session_id BIGINT NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    note TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_session_events_session
        FOREIGN KEY (session_id) REFERENCES check_in_sessions(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_user_roles_role_id
    ON user_roles(role_id);

CREATE INDEX idx_responder_availability_responder_id
    ON responder_availability(responder_id);

CREATE INDEX idx_availability_time
    ON responder_availability(available_from, available_until);

CREATE INDEX idx_sessions_customer_id
    ON check_in_sessions(customer_id);

CREATE INDEX idx_sessions_responder_id
    ON check_in_sessions(responder_id);

CREATE INDEX idx_session_events_session_id
    ON session_events(session_id);

INSERT INTO roles (name) VALUES
('ADMIN'),
('CUSTOMER'),
('RESPONDER');

INSERT INTO check_in_methods (name) VALUES
('PHONE');