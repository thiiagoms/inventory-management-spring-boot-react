ALTER TABLE suppliers
    ADD CONSTRAINT uk_suppliers_social_name UNIQUE (social_name);
