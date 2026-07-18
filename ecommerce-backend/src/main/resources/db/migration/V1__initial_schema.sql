CREATE TABLE address
(
    id      BIGINT AUTO_INCREMENT NOT NULL,
    street  VARCHAR(255)          NULL,
    city    VARCHAR(255)          NULL,
    state   VARCHAR(255)          NULL,
    country VARCHAR(255)          NULL,
    zipcode VARCHAR(255)          NULL,
    user_id BIGINT                NULL,
    CONSTRAINT pk_address PRIMARY KEY (id)
);

CREATE TABLE products
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    name           VARCHAR(255)          NULL,
    `description`  VARCHAR(255)          NULL,
    price          DECIMAL               NULL,
    stock_quantity INT                   NULL,
    category       VARCHAR(255)          NULL,
    image_url      VARCHAR(255)          NULL,
    active         BIT(1)                NULL,
    created_at     datetime              NULL,
    updated_at     datetime              NULL,
    CONSTRAINT pk_products PRIMARY KEY (id)
);

CREATE TABLE revchanges
(
    rev        BIGINT       NOT NULL,
    entityname VARCHAR(255) NULL
);

CREATE TABLE revinfo
(
    rev      BIGINT NOT NULL,
    revtstmp BIGINT NULL,
    CONSTRAINT pk_revinfo PRIMARY KEY (rev)
);

CREATE TABLE user
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    first_name VARCHAR(255)          NULL,
    last_name  VARCHAR(255)          NULL,
    email      VARCHAR(255)          NULL,
    password   VARCHAR(255)          NULL,
    `role`     VARCHAR(255)          NULL,
    phone      VARCHAR(255)          NULL,
    created_at datetime              NULL,
    updated_at datetime              NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

ALTER TABLE address
    ADD CONSTRAINT uc_address_user UNIQUE (user_id);

ALTER TABLE address
    ADD CONSTRAINT FK_ADDRESS_USER FOREIGN KEY (user_id) REFERENCES user (id);

ALTER TABLE revchanges
    ADD CONSTRAINT fk_revchanges_on_default_tracking_modified_entities_changelog FOREIGN KEY (rev) REFERENCES revinfo (rev);