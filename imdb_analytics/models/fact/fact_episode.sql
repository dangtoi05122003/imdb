select
    title_id,
    parent_title_id,
    season_number,
    episode_number
from {{source('imdb', 'title_episode')}}