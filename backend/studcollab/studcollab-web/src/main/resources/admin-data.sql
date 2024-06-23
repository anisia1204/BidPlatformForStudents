DO
'
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.table_constraints
        WHERE table_name = ''users''
          AND constraint_name = ''unique_email''
    ) THEN
ALTER TABLE users
    ADD CONSTRAINT unique_email UNIQUE (email);
END IF;
END;
' LANGUAGE PLPGSQL;
INSERT INTO users (email, first_name, last_name, password, points, role, enabled, created_at)
VALUES ('admin@admin', 'admin', 'admin', '$2a$12$5./TkSHvatIxSdvEeH3gz.1ILgHiYj6jauYcPgW5vMOXCUeD0RbWS', 0, 1, true, '2024-04-25T23:40:48')
    ON CONFLICT (email) DO NOTHING;
