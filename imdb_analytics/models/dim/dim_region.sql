select distinct
    region,
    language
from {{source ('imdb', 'title_akas')}}
where trim(region) != ''