create or replace function core_get_player_data_by_id_and_name(player_id uuid, name text) returns players
    language plpgsql
as
$$
declare
    player players;
begin
    -- Select the player from the database
    select * from players where id = player_id into player;

    -- If the player doesn't exist, create a new one
    if not found then
        insert into players (id, name) values (player_id, $2) returning * into player;
    end if;

    return player;
end;
$$;

create or replace procedure core_update_player_data(player_id uuid, current_name text, current_tag int)
    language sql
as
$$
update players
set tag  = current_tag,
    name = current_name
    where id = player_id;
$$