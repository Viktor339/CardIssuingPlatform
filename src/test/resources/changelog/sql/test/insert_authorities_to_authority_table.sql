--liquibase formatted sql
--changeset ViktorBelous:10

insert into authority (authority_name, created_time, user_id, created_by_id)
values ('READ_INTERMEDIATE_CARD_STATUS','2022-02-04 11:29:20.000000', 2, 2);


--rollback delete from authority where authority_name = 'READ_INTERMEDIATE_CARD_STATUS' and user_id = 2;
