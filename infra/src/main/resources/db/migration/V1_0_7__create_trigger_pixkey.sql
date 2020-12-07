create or replace function log_content_identifier_event()
    returns trigger
    language plpgsql
as
$$
declare
    type_added   constant character varying(20) := 'ADDED';
    type_removed constant character varying(20) := 'REMOVED';
BEGIN

    if (tg_op = 'INSERT') then
        insert into dict.content_identifier_event(pix_key, key_type, cid, created_at, key_ownership_date, type)
        values (new.key, new.type, new.cid, now(), new.ownership_date, type_added);
    end if;

    if (tg_op = 'DELETE') then
        insert into dict.content_identifier_event(pix_key, key_type, cid, created_at, key_ownership_date, type)
        values (old.key, old.type, old.cid, now(), now(), type_removed);
    end if;

    if (tg_op = 'UPDATE') then
        insert into dict.content_identifier_event(pix_key, key_type, cid, created_at, key_ownership_date, type)
        values (new.key, new.type, new.cid, now(), new.ownership_date, type_added);

        insert into dict.content_identifier_event(pix_key, key_type, cid, created_at, key_ownership_date, type)
        values (old.key, old.type, old.cid, now(), old.ownership_date, type_removed);
    end if;

    return new;
END;
$$;

create trigger log_content_identifier_event
    after insert
        or update of type, key, ownership_date, name, participant, branch, account_number, account_type, request_id
        or delete
    on dict.pix_key
    for each row
execute procedure log_content_identifier_event();