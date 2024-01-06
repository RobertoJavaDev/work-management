--liquibase formatted sql

--changeset RobertoJavaDev:002_1

create table if not exists users
(
    id              uuid not null primary key,
    user_name       varchar(20) not null,
    email           varchar(100),
    password        varchar,
    creation_date   timestamp,
    user_status     varchar(30)
    )