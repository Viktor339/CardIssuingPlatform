--liquibase formatted sql
--changeset ViktorBelous:1
create table if not exists roles
(
    id bigserial not null
    constraint roles_pkey
    primary key,
    role_name varchar(255) not null
    );

alter table roles owner to postgres;
--rollback drop table roles;


--changeset ViktorBelous:2
create table if not exists users
(
    id bigserial not null
    constraint users_pkey
    primary key,
    created_at timestamp not null,
    date_of_birth date not null,
    email varchar(255) not null,
    first_name varchar(255) not null,
    is_enabled boolean not null,
    last_name varchar(255) not null,
    password varchar(255) not null,
    username varchar(255) not null,
    role_id smallint not null
    constraint fkp56c1712k691lhsyewcssf40f
    references roles
    );

alter table users owner to postgres;
--rollback drop table users;






