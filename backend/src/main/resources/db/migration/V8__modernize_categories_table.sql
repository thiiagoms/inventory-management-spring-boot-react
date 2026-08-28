ALTER TABLE products DROP FOREIGN KEY fk_products_category;

ALTER TABLE categories ADD COLUMN uuid BINARY(16);
UPDATE categories SET uuid = UUID_TO_BIN(UUID()) WHERE uuid IS NULL;
ALTER TABLE categories MODIFY COLUMN uuid BINARY(16) NOT NULL;

ALTER TABLE products ADD COLUMN category_uuid BINARY(16);
UPDATE products product
INNER JOIN categories category ON category.id = product.category_id
SET product.category_uuid = category.uuid;

ALTER TABLE products DROP COLUMN category_id;
ALTER TABLE categories MODIFY COLUMN id BIGINT NOT NULL;
ALTER TABLE categories DROP PRIMARY KEY;
ALTER TABLE categories DROP COLUMN id;
ALTER TABLE categories CHANGE COLUMN uuid id BINARY(16) NOT NULL;
ALTER TABLE categories ADD CONSTRAINT pk_categories PRIMARY KEY (id);

ALTER TABLE products CHANGE COLUMN category_uuid category_id BINARY(16);
ALTER TABLE products
    ADD CONSTRAINT fk_products_category
    FOREIGN KEY (category_id) REFERENCES categories (id);

ALTER TABLE categories CHANGE COLUMN name title VARCHAR(250) NOT NULL;
ALTER TABLE categories ADD COLUMN description TEXT;
UPDATE categories SET description = title WHERE description IS NULL;
ALTER TABLE categories MODIFY COLUMN description TEXT NOT NULL;
ALTER TABLE categories ADD COLUMN updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6);
ALTER TABLE categories ADD CONSTRAINT uk_categories_title UNIQUE (title);
