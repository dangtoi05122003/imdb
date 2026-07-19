select
    person_id,
    trim(profession) as profession
from {{source ('imdb', 'name_basics')}},
unnest(split(primary_professions, ',')) as profession