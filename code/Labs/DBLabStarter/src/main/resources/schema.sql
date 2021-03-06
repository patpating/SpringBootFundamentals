DROP TABLE TRACK IF EXISTS;

CREATE USER IF NOT EXISTS TRACKER SALT 'f2d97d5e5c194fe4' HASH 'bf9ac7082b79123183a1a58f3f23b3822cbedc5c1161394f43bd4d0d03237c59' ADMIN;
CREATE MEMORY TABLE PUBLIC.TRACK(
    ID BIGINT identity primary key NOT NULL,
    TITLE VARCHAR(255),
    ARTIST VARCHAR(255),
    ALBUM VARCHAR(255),
    DURATION VARCHAR(255),
    DATE VARCHAR(255)
);
