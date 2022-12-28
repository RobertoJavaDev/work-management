--liquibase formatted sql

--changeset RobertoJavaDev:001_1

create table if not exists tasks
(
    id          uuid not null primary key,
    description varchar(100) not null,
    done        bit,
    deadline datetime null
)

--changeset RobertoJavaDev:001_2

insert into tasks (id, description, done) values
    (random_uuid(), 'Learn programming', false),
    (random_uuid(), 'Write application', false);

--changeset RobertoJavaDev:001_3

create table if not exists projects
(
    id              uuid not null primary key,
    description     varchar(100) not null,
    creation_date   datetime not null
)

--changeset RobertoJavaDev:001_4

create table if not exists sections
(
    id          uuid not null primary key,
    description varchar(100) not null
)

--changeset RobertoJavaDev:001_5

alter table sections
    add column project_id uuid null;
alter table sections
    add foreign key (project_id) references projects (id);

--changeset RobertoJavaDev:001_6

alter table tasks
    add column task_section_id uuid null;
alter table tasks
    add foreign key (task_section_id) references sections (id);

--changeset RobertoJavaDev:001_7

alter table projects
    add column name varchar(100) not null;