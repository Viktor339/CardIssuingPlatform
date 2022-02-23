--liquibase formatted sql
--changeset ViktorBelous:6
insert into users (created_at, date_of_birth, email, first_name, is_enabled, last_name, password, username,
                   company_id, role_id)
values ('2022-02-04 11:29:20.000000', '2022-02-04', 'tom', 'Tom', true, 'Hanks', '$2a$10$ghv.w5buRAfqcDnY0dKPzumov8gnjXaBBoxOWuqSQeHu7z37J2OwW', 'tom', 2, 1);


insert into users (created_at, date_of_birth, email, first_name, is_enabled, last_name, password, username,
                   company_id, role_id)
values ('2022-02-04 11:29:20.000000', '2022-02-04', 'email', 'Bred', true, 'Pitt', 1, 'bred', 1, 2);

insert into users (created_at, date_of_birth, email, first_name, is_enabled, last_name, password, username,
                   company_id, role_id)
values ('2022-02-04 11:29:20.000000', '2022-02-04', 'admin', 'admin', true, 'admin', '$2a$10$8y.88Q3jvzlqArLQwW9JHOYwYWoDqVDdCpe1ePgr58b4Ne796mqMK', 'admin', 1, 1);

insert into users (created_at, date_of_birth, email, first_name, is_enabled, last_name, password, username,
                   company_id, role_id)
values ('2022-02-04 11:29:20.000000', '2022-02-04', 'accountant', 'accountant', true, 'accountant', '$2a$10$QdAmiskbyPT4EXf9.GSNT.3w87khAo8NN4DSZSW66WY1UN6P5Jg7.', 'accountant', 1, 3);


--rollback delete from users where first_name = 'Bred' or first_name = 'Tom';