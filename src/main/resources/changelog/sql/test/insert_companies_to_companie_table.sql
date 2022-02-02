--liquibase formatted sql
--changeset ViktorBelous:4
insert into companies (name,is_enabled)
values ('A',true);

insert into companies (name,is_enabled)
values ('B',true);

insert into companies (name,is_enabled)
values ('C',true);
--rollback delete from companies where name = 'A' or name = 'B' or name = 'C';


