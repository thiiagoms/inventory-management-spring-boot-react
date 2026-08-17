CREATE TABLE suppliers (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    contact VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    created_at DATETIME(6),
    CONSTRAINT pk_suppliers PRIMARY KEY (id)
);
