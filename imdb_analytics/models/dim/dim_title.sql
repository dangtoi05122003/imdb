select
    title_id,
    title_type,
    primary_title,
    original_title,
    is_adult,
    start_year,
    end_year,
    runtime_minutes
from {{source ('imdb', 'title_basics')}}