CREATE TABLE IF NOT EXISTS PRODUCTS
(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1000001) PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS ARTICLES
(
    ID BIGINT PRIMARY KEY,
    NAME VARCHAR(255) NOT NULL,
    STOCK INT NOT NULL
);

CREATE TABLE IF NOT EXISTS PRODUCT_ARTICLE_DETAILS
(
    ID BIGINT GENERATED BY DEFAULT AS IDENTITY (START WITH 1000001) PRIMARY KEY,
    PRODUCT_ID BIGINT NOT NULL,
    ARTICLE_ID BIGINT NOT NULL,
    AMOUNT_OF INT NOT NULL,
    FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCTS(ID),
    FOREIGN KEY (ARTICLE_ID) REFERENCES ARTICLES(ID)
);

CREATE INDEX product_name_index ON PRODUCTS (NAME);
