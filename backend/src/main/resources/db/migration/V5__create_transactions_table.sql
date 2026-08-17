CREATE TABLE transactions (
    id BIGINT NOT NULL AUTO_INCREMENT,
    total_products INTEGER NOT NULL,
    total_price DECIMAL(38, 2) NOT NULL,
    transaction_type ENUM ('PURCHASE', 'SALE', 'RETURN_TO_SUPPLIER') NOT NULL,
    transaction_status ENUM ('PENDING', 'PROCESSING', 'COMPLETED', 'CANCELLED') NOT NULL,
    descritpion VARCHAR(255) NOT NULL,
    note VARCHAR(255) NOT NULL,
    created_at DATETIME(6),
    updated_at DATETIME(6),
    product_id BIGINT,
    user_id BIGINT,
    supplier_id BIGINT,
    CONSTRAINT pk_transactions PRIMARY KEY (id),
    CONSTRAINT fk_transactions_product
        FOREIGN KEY (product_id) REFERENCES products (id),
    CONSTRAINT fk_transactions_user
        FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_transactions_supplier
        FOREIGN KEY (supplier_id) REFERENCES suppliers (id)
);
