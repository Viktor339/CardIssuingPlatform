--liquibase formatted sql
--changeset ViktorBelous:9

create table if not exists authority
(
    authority_name varchar   not null,
    created_time   timestamp not null,
    user_id        bigint    not null
        constraint fkka37hl6mopj61rfbe97si18p8
            references users,
    created_by_id  bigint
        constraint fkfjij7c24kv2mlhh5nf0vgdyoq
            references users,
    primary key (authority_name, user_id)
);

--rollback drop table authority;