select distinct
    title_id,
    trim(genre) as genre
from {{source ('imdb', 'title_basics')}},
unnest(split(genres, ',')) as genre
where trim(genre) != ''