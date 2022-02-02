--liquibase formatted sql
--changeset ViktorBelous:1
create table if not exists roles
(
    id bigserial not null constraint roles_pkey
    primary key,
    role_name varchar(255) not null
    );

--rollback drop table roles;

--changeset ViktorBelous:2
create table if not exists companies
(
    id bigserial not null constraint companies_pkey
    primary key,
    is_enabled boolean,
    name  varchar(255) not null
    );

--rollback drop table companies;


--changeset ViktorBelous:3
create table if not exists users
(
    id bigserial not null constraint users_pkey
    primary key,
    created_at timestamp not null,
    date_of_birth date not null,
    email varchar(255) not null,
    first_name varchar(255) not null,
    is_enabled boolean not null,
    last_name varchar(255) not null,
    password varchar(255) not null,
    username varchar(255) not null,
    company_id bigint
    constraint fkin8gn4o1hpiwe6qe4ey7ykwq7
    references companies,
    role_id smallint not null
    constraint fkp56c1712k691lhsyewcssf40f
    references roles
    );

--rollback drop table users;



--changeset ViktorBelous:5
create table if not exists cards
(
    id bigserial not null constraint cards_pkey
    primary key,
    created_by  bigint      not null,
    currency    varchar(3)   not null,
    first_name  varchar(255) not null,
    is_active   boolean      not null,
    last_name   varchar(255) not null,
    number      varchar(4)   not null,
    type        varchar(255) not null,
    valid_till  date         not null,
    company_id  bigint       not null
    constraint fkbk2y19sqrqsv8gc6xw4gi5eyc
    references companies,
    owned_by_id bigint
    constraint fkdpkoa5sm3hx0bgiifpjabqdlb
    references users
    );

--rollback drop table cards;



--changeset ViktorBelous:6
create table if not exists card_status
(
    id bigserial not null constraint card_status_pkey
    primary key,
    created         timestamp  not null,
    previous_status varchar(3),
    status          varchar(3) not null,
    card_id         bigint
    constraint fkarp7uu64f4y3ow21439lyqxa2
    references cards
    );
--rollback drop table card_status;



