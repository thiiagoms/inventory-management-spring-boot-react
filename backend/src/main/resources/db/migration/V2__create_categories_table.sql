CREATE TABLE categories (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    created_at DATETIME(6),
    CONSTRAINT pk_categories PRIMARY KEY (id)
);
