--liquibase formatted sql
--changeset ViktorBelous:7

insert into cards (created_by, currency, first_name, is_active, last_name, number, type, valid_till, company_id,
                   owned_by_id)
values (1, 978, 'Tommy', true, 'Hanks', 5555, 'PERSONAL', '2022-02-04', 1, 1);


insert into cards (created_by, currency, first_name, is_active, last_name, number, type, valid_till, company_id,
                   owned_by_id)
values (1, 978, 'Bready', true, 'Pitt', 5555, 'PERSONAL', '2022-02-04', 1, 2);

--rollback delete from cards where first_name = 'Tommy' or name = 'Bready';