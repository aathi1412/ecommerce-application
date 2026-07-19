CREATE TABLE cart_item
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    product_id BIGINT                NOT NULL,
    user_id    BIGINT                NOT NULL,
    quantity   INT                   NULL,
    price      DECIMAL               NULL,
    created_at datetime              NULL,
    updated_at datetime              NULL,
    CONSTRAINT pk_cartitem PRIMARY KEY (id)
);

ALTER TABLE cart_item
    ADD CONSTRAINT FK_CARTITEM_ON_PRODUCT FOREIGN KEY (product_id) REFERENCES products (id);

ALTER TABLE cart_item
    ADD CONSTRAINT FK_CARTITEM_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);