create or replace function api_create_new_api_key(
    player_id uuid
) returns uuid
    language sql as
$$
insert into api_keys (player_id, key)
values ($1, gen_random_uuid())
on conflict (player_id) do update set key = excluded.key
returning key;
$$;