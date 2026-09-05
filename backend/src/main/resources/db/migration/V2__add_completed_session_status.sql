ALTER TABLE check_in_sessions
    DROP CONSTRAINT chk_sessions_status;

ALTER TABLE check_in_sessions
    ADD CONSTRAINT chk_sessions_status
        CHECK (status IN (
            'PLANNED',
            'ACTIVE',
            'CHECKED_IN',
            'MISSED',
            'ESCALATED',
            'COMPLETED',
            'CANCELLED'
        ));
