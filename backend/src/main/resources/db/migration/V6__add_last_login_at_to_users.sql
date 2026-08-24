ALTER TABLE users
    ADD COLUMN last_login_at DATETIME(6) NULL AFTER created_at;
