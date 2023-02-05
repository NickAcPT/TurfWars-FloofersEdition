-- get_or_create_metric function
-- This function returns the metrics record given a metric name
-- If the metric doesn't exist, it creates a new one and returns it
create or replace function metrics_get_or_create_metric(
    metric_name text
) returns metrics
    language plpgsql as
$$

declare
    metric metrics;
begin
    -- Try to find the metric in the metrics table
    select *
    from metrics
    where name = metric_name
    into metric;

    -- If the metric was found, return it
    if found then
        return metric;
    else
        -- If the metric was not found, insert a new one with the given name
        insert into metrics (name)
        values (metric_name)
        returning * into metric;

        return metric;
    end if;
end;
$$;

-- insert_player_metric procedure
-- This procedure inserts a new player_metric record into the player_metrics table
-- The parameters are:
-- - player_id: The id of the player
-- - metric_id: The id of the metric
-- - value: The value of the metric for the player
create or replace procedure metrics_insert_player_metric(
    player_id uuid,
    metric_id int,
    value int
)
    language sql as
$$
insert into player_metrics (player_id, metric_id, value)
values ($1, $2, $3)
$$;

-- get_top_players function
-- This function returns a table with the player_id and value for the top players of a given metric
-- The metric_id parameter specifies the metric to get the top players for
-- The table is ordered by value in descending order
create or replace function metrics_get_top_players(
    metric_id int
)
    returns table
            (
                player_id uuid,
                value     int
            )
    language sql
as
$$
select pm.player_id as player_id, sum(pm.value) as value
from player_metrics pm
where pm.metric_id = $1
group by pm.player_id
order by value desc
$$;

-- compact_player_metrics procedure
-- This procedure compacts the player_metrics table by merging all records with the same player_id and metric_id
-- The resulting table has unique combinations of player_id and metric_id with the sum of all values for each combination
create or replace procedure metrics_compact_player_metrics()
    language plpgsql as
$$
begin
    -- Create a temporary table with the compacted player_metrics data
    create temporary table temp_player_metrics as
    select player_id, metric_id, sum(value) as total_value
    from player_metrics
    group by player_id, metric_id;

    -- Truncate the player_metrics table to clear its data
    truncate player_metrics restart identity;

    -- Insert the compacted data from the temporary table back into the player_metrics table
    INSERT INTO player_metrics (player_id, metric_id, value)
    SELECT player_id, metric_id, total_value
    FROM temp_player_metrics;

    -- Drop the temporary table
    DROP TABLE temp_player_metrics;
end;
$$;