select
    person_id,
    primary_name,
    birth_year,
    death_year
from  {{source('imdb', 'name_basics')}}