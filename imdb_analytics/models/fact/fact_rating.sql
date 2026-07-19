select
    title_id,
    average_rating,
    num_votes
from {{source ('imdb', 'title_ratings')}}