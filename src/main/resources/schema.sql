CREATE TABLE if not exists appliances
(
    id           BIGSERIAL PRIMARY KEY,
    amount       int default 0,
    brand        VARCHAR(256) NOT NULL,
    equipment    VARCHAR(256) NOT NULL
);
