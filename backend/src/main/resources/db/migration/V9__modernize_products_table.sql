ALTER TABLE transactions DROP FOREIGN KEY fk_transactions_product;

ALTER TABLE products ADD COLUMN uuid BINARY(16);
UPDATE products SET uuid = UUID_TO_BIN(UUID()) WHERE uuid IS NULL;
ALTER TABLE products MODIFY COLUMN uuid BINARY(16) NOT NULL;

ALTER TABLE transactions ADD COLUMN product_uuid BINARY(16);
UPDATE transactions transaction_record
INNER JOIN products product ON product.id = transaction_record.product_id
SET transaction_record.product_uuid = product.uuid;

ALTER TABLE transactions DROP COLUMN product_id;
ALTER TABLE products MODIFY COLUMN id BIGINT NOT NULL;
ALTER TABLE products DROP PRIMARY KEY;
ALTER TABLE products DROP COLUMN id;
ALTER TABLE products CHANGE COLUMN uuid id BINARY(16) NOT NULL;
ALTER TABLE products ADD CONSTRAINT pk_products PRIMARY KEY (id);

ALTER TABLE transactions CHANGE COLUMN product_uuid product_id BINARY(16);
ALTER TABLE transactions
    ADD CONSTRAINT fk_transactions_product
    FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE products CHANGE COLUMN name title VARCHAR(250) NOT NULL;
ALTER TABLE products CHANGE COLUMN descritpion description VARCHAR(255) NOT NULL;
ALTER TABLE products MODIFY COLUMN created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6);
ALTER TABLE products ADD COLUMN updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6);
ALTER TABLE products ADD CONSTRAINT uk_products_title UNIQUE (title);
