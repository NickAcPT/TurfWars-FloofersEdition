create or replace function core_get_player_data_by_id(player_id uuid) returns players
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
        insert into players (id) values (player_id) returning * into player;
    end if;

    return player;
end;
$$;

create or replace procedure core_update_player_data(player_id uuid, current_tag int)
    language sql
as
$$
update players
set tag = current_tag
where id = player_id;
$$