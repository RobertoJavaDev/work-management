--liquibase formatted sql

--changeset RobertoJavaDev:001_1

create table if not exists tasks
(
    id          uuid not null primary key,
    description varchar(100) not null,
    done        bit,
    deadline datetime null
)