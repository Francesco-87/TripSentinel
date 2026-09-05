ALTER TABLE check_in_sessions
    ALTER COLUMN time_zone DROP DEFAULT;

ALTER TABLE responder_availability
    ALTER COLUMN time_zone DROP DEFAULT;
