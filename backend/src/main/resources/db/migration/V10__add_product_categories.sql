CREATE TABLE product_categories (
    product_id BINARY(16) NOT NULL,
    category_id BINARY(16) NOT NULL,
    CONSTRAINT pk_product_categories PRIMARY KEY (product_id, category_id),
    CONSTRAINT fk_product_categories_product
        FOREIGN KEY (product_id) REFERENCES products (id) ON DELETE CASCADE,
    CONSTRAINT fk_product_categories_category
        FOREIGN KEY (category_id) REFERENCES categories (id)
);

INSERT INTO product_categories (product_id, category_id)
SELECT id, category_id
FROM products
WHERE category_id IS NOT NULL;

ALTER TABLE products DROP FOREIGN KEY fk_products_category;
ALTER TABLE products DROP COLUMN category_id;
