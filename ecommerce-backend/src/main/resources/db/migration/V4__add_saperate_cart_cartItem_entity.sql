ALTER TABLE cart_item
    DROP FOREIGN KEY FK_CARTITEM_ON_USER;

CREATE TABLE cart
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    user_id    BIGINT                NULL,
    created_at datetime              NULL,
    updated_at datetime              NULL,
    CONSTRAINT pk_cart PRIMARY KEY (id)
);

ALTER TABLE cart_item
    ADD added_at datetime NULL;

ALTER TABLE cart_item
    ADD cart_id BIGINT NULL;

ALTER TABLE cart_item
    MODIFY cart_id BIGINT NOT NULL;

ALTER TABLE cart_item
    ADD CONSTRAINT FK_CARTITEM_ON_CART FOREIGN KEY (cart_id) REFERENCES cart (id);

ALTER TABLE cart
    ADD CONSTRAINT FK_CART_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE cart_item
    DROP COLUMN created_at;

ALTER TABLE cart_item
    DROP COLUMN updated_at;

ALTER TABLE cart_item
    DROP COLUMN user_id;