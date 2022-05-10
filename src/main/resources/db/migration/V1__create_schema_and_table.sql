-- noinspection SqlNoDataSourceInspectionForFile

/**
 * Author:  Alex Usenko
 */

/* Create separate schema */
-- CREATE SCHEMA IF NOT EXISTS student_api;

CREATE TABLE student
(
    id                   SERIAL      NOT NULL PRIMARY KEY,
    phone                VARCHAR(64) NOT NULL,
    name                 VARCHAR(64) NOT NULL,
    email                VARCHAR(64) NULL,
    state                VARCHAR(64) NOT NULL default 'NEW',
    created              TIMESTAMP   NOT NULL
);

CREATE UNIQUE INDEX st_uidx ON student (phone);