from airflow import DAG
from airflow.operators.bash import BashOperator
from datetime import datetime, timedelta
from models.base import BaseDagConfig
from generators.base import Base
from abc import abstractmethod, ABC

class DagGenerator(Base, ABC):
    def __init__(self, path, layer):
        super().__init__(path)
        self.layer = layer
    def load_dags(self, global_session):
        for path in self.load_config_path():
            data = self.load_file(path)
            default = data.get("default_args")
            for config in data.get("dag_configs"):
                tasks = config.get("tasks")
                conf = BaseDagConfig(**{**default, **{k:v for k, v in config.items() if k != "tasks"}})
                global_session[conf.dag_id] = self.create_airflow_dag(conf, tasks)
    def create_airflow_dag(self, conf, tasks):
        dag = DAG(
            dag_id = conf.dag_id,
            default_args= {
                'owner': conf.owner,
                'retries': conf.retries,
                'retry_delay': timedelta(minutes=conf.retry_delay),
            },
            start_date=datetime.strptime(conf.start_date, '%Y-%m-%d'),
            schedule_interval = conf.schedule_interval,
            max_active_runs = conf.max_active_runs,
            catchup = conf.catchup,
            tags=[self.layer]
        )
        with dag:
            for task in tasks:
                BashOperator(
                    task_id = task['task_id'],
                    bash_command = self.get_bash_command(task),
                    cwd=task.get("cwd")
                )
        return dag
    @abstractmethod
    def get_bash_command(self, task):
        pass