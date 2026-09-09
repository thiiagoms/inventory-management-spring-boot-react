ALTER TABLE suppliers ADD COLUMN uuid BINARY(16);
UPDATE suppliers SET uuid = UUID_TO_BIN(UUID()) WHERE uuid IS NULL;
ALTER TABLE suppliers MODIFY COLUMN uuid BINARY(16) NOT NULL;

ALTER TABLE transactions DROP FOREIGN KEY fk_transactions_supplier;
ALTER TABLE transactions ADD COLUMN supplier_uuid BINARY(16);
UPDATE transactions transaction_record
INNER JOIN suppliers supplier ON supplier.id = transaction_record.supplier_id
SET transaction_record.supplier_uuid = supplier.uuid;
ALTER TABLE transactions DROP COLUMN supplier_id;

ALTER TABLE suppliers MODIFY COLUMN id BIGINT NOT NULL;
ALTER TABLE suppliers DROP PRIMARY KEY;
ALTER TABLE suppliers DROP COLUMN id;
ALTER TABLE suppliers CHANGE COLUMN uuid id BINARY(16) NOT NULL;
ALTER TABLE suppliers ADD CONSTRAINT pk_suppliers PRIMARY KEY (id);

ALTER TABLE suppliers CHANGE COLUMN name social_name VARCHAR(250) NOT NULL;
ALTER TABLE suppliers CHANGE COLUMN contact cnpj VARCHAR(14) NOT NULL;
UPDATE suppliers SET cnpj = REGEXP_REPLACE(cnpj, '[^0-9]', '');
ALTER TABLE suppliers MODIFY COLUMN address VARCHAR(500) NOT NULL;
ALTER TABLE suppliers MODIFY COLUMN created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6);
ALTER TABLE suppliers ADD COLUMN updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6);
ALTER TABLE suppliers ADD CONSTRAINT uk_suppliers_cnpj UNIQUE (cnpj);

ALTER TABLE transactions CHANGE COLUMN supplier_uuid supplier_id BINARY(16);
ALTER TABLE transactions
    ADD CONSTRAINT fk_transactions_supplier
    FOREIGN KEY (supplier_id) REFERENCES suppliers (id);

ALTER TABLE products ADD COLUMN supplier_id BINARY(16);

INSERT INTO suppliers (id, social_name, cnpj, address, created_at, updated_at)
SELECT UUID_TO_BIN(UUID()), 'Legacy Supplier', '00000000000191', 'Address not provided', NOW(6), NOW(6)
WHERE EXISTS (SELECT 1 FROM products) AND NOT EXISTS (SELECT 1 FROM suppliers);

UPDATE products
SET supplier_id = (SELECT id FROM suppliers ORDER BY created_at, id LIMIT 1)
WHERE supplier_id IS NULL;

ALTER TABLE products MODIFY COLUMN supplier_id BINARY(16) NOT NULL;
ALTER TABLE products
    ADD CONSTRAINT fk_products_supplier
    FOREIGN KEY (supplier_id) REFERENCES suppliers (id);
