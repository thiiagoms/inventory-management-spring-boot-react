CREATE TABLE products (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    descritpion VARCHAR(255) NOT NULL,
    sku VARCHAR(255) NOT NULL,
    image_url VARCHAR(255) NOT NULL,
    price DECIMAL(38, 2) NOT NULL,
    stock_quantity INTEGER NOT NULL,
    category_id BIGINT,
    expiry_date DATETIME(6) NOT NULL,
    created_at DATETIME(6),
    CONSTRAINT pk_products PRIMARY KEY (id),
    CONSTRAINT uk_products_sku UNIQUE (sku),
    CONSTRAINT fk_products_category
        FOREIGN KEY (category_id) REFERENCES categories (id)
);
