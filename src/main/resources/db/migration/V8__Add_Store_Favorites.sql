CREATE TABLE user_store_favorites(
    user_id VARCHAR(255) NOT NULL,
    store_id VARCHAR(255) NOT NULL,
    CONSTRAINT FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT FOREIGN KEY (store_id) REFERENCES stores(id),
    PRIMARY KEY (user_id, store_id)
)