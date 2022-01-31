--liquibase formatted sql
--changeset ViktorBelous:4
insert into roles (role_name)
values ('ROLE_ADMIN');

insert into roles (role_name)
values ('ROLE_USER');

insert into roles (role_name)
values ('ROLE_ACCOUNTANT');
--rollback delete from roles where role_name = 'ROLE_USER' or role_name = 'ROLE_ACCOUNTANT' or role_name = 'ROLE_ADMIN';


