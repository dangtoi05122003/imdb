select distinct
    category,
    job
from {{source ('imdb', 'title_principals')}}