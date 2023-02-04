create function core_get_tags() returns setof player_tags
    language sql
as
$$
select *
from player_tags
$$;

create procedure core_add_tag(name text, value text, admin_only bool)
    language sql
as
$$
insert into player_tags (name, component, admin_only)
values ($1, $2, $3)
$$;

create procedure core_remove_tag(id int)
    language sql
as
$$
delete
from player_tags
where player_tags.id = $1
$$;