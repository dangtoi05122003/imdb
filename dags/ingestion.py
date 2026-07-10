from datetime import datetime, timedelta
from airflow import DAG
from airflow.operators.bash import BashOperator

default_args = {
    'owner': 'imdb',
    'retries': 0,
    "retry_delay": timedelta(minutes=5)
}

with DAG(
    dag_id = 'ingestion_dag',
    default_args = default_args,
    start_date = datetime(2025, 5,5),
    schedule_interval = None,
    catchup = False,
    max_active_runs = 1
) as dag:
    BashOperator(
        task_id='bronze',
        bash_command=(
            'java '
            '--add-opens=java.base/java.nio=ALL-UNNAMED '
            '--add-opens=java.base/sun.nio.ch=ALL-UNNAMED '
            '-cp /opt/airflow/app/my-spark-app.jar com.imdb.bronze.Ingestion'
        ),
        cwd='/opt/airflow'
    )
