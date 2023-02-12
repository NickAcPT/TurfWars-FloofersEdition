create or replace view metrics as
SELECT *
from public.metrics;

grant select on api.metrics to web_anon;

create or replace function api.filter_api_token() returns void
    security definer as
$$
declare
    headers    json := current_setting('request.headers', true)::json;
    api_token  text;
    authorized boolean;
begin
    api_token := coalesce(headers ->> 'api-token', '');

    authorized := (select exists (select 1 from public.api_keys where key::text = api_token));

    if not authorized then
        raise sqlstate 'PT401' using
            message = 'Invalid API key.',
            detail = 'The API key provided in the Authorization header is either missing or invalid',
            hint = 'Please provide a valid API key in the Authorization header. ' ||
                   'To obtain an API key, please join the server and run /api new.';
    end if;
end;
$$ language plpgsql;