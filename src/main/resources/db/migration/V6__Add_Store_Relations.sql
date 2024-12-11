ALTER TABLE products
    ADD COLUMN store_id VARCHAR(255),
    ADD CONSTRAINT FOREIGN KEY (store_id) REFERENCES stores(id)
        ON DELETE CASCADE;

ALTER TABLE users
    ADD COLUMN store_id VARCHAR(255) NULL,
    ADD CONSTRAINT FOREIGN KEY (store_id) REFERENCES stores(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE;