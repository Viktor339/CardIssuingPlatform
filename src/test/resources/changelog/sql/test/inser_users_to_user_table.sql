--liquibase formatted sql
--changeset ViktorBelous:6
insert into users (created_at, date_of_birth, email, first_name, is_enabled, last_name, password, username,
                   company_id, role_id)
values ('2022-02-04 11:29:20.000000', '2022-02-04', 'email', 'Tom', true, 'Hanks', 1, 'th', 1, 1);


insert into users (created_at, date_of_birth, email, first_name, is_enabled, last_name, password, username,
                   company_id, role_id)
values ('2022-02-04 11:29:20.000000', '2022-02-04', 'email', 'Bred', true, 'Pitt', 1, 'th', 1, 1);


--rollback delete from users where first_name = 'Bred' or first_name = 'Tom';