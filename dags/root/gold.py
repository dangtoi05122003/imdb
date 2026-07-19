import os
from generators.gold import GoldGenerator

CONFIG_PATH = os.getenv("AIRFLOW_GOLD_PATH")

generator = GoldGenerator(path=CONFIG_PATH, layer= 'gold')
generator.load_dags(globals())