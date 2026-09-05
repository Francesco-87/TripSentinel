ALTER TABLE check_in_sessions
    ADD COLUMN time_zone VARCHAR(63) NOT NULL DEFAULT 'UTC' AFTER latest_check_in_at;

ALTER TABLE responder_availability
    ADD COLUMN time_zone VARCHAR(63) NOT NULL DEFAULT 'UTC' AFTER available_until;
