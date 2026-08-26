ALTER TABLE users
    ADD COLUMN updated_at DATETIME(6) NULL AFTER created_at;

UPDATE users
SET created_at = COALESCE(created_at, CURRENT_TIMESTAMP(6)),
    updated_at = COALESCE(created_at, CURRENT_TIMESTAMP(6));

ALTER TABLE users
    MODIFY created_at DATETIME(6) NOT NULL,
    MODIFY updated_at DATETIME(6) NOT NULL;
