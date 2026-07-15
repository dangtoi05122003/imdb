import attr

@attr.s(auto_attribs=True, kw_only=True)
class BaseDagConfig:
    owner: str
    retries: int
    retry_delay: int
    dag_id: str
    schedule_interval: str
    max_active_runs: int
    catchup: bool
    start_date: str
