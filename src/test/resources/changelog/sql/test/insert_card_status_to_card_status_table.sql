--liquibase formatted sql
--changeset ViktorBelous:8

insert into card_status (created, previous_status, status, card_id)
values ('2022-02-04 11:29:20.000000', 0,100, 1);

insert into card_status (created, previous_status, status, card_id)
values ('2022-02-04 11:29:20.000000', 0,200, 2);

--rollback delete from card_status where created = '2022-02-04 11:29:20.000000';
