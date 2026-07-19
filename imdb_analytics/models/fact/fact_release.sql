select
    title_id,
    ordering,
    title,
    region,
    language,
    types,
    attributes,
    is_original_title
from {{source('imdb', 'title_akas')}}