select
    title_id,
    person_id,
    category,
    job,
    ordering
from {{source ('imdb', 'title_principals')}}