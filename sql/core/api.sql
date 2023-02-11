create or replace function api_create_new_api_key(
    player_id uuid
) returns uuid
    language plpgsql as
$$
declare
    new_key uuid;
begin
    new_key = gen_random_uuid();

    -- Try to insert a new record for the player with a new UUID value for the key
    begin
        insert into api_keys (player_id, key)
        values ($1, new_key);
        return new_key;
    exception
        when unique_violation then
            -- If the insertion fails, update the existing key value for the player
            -- to a new UUID value
            update api_keys
            set key = new_key
            where api_keys.player_id = $1;
            return new_key;
    end;
end;
$$;