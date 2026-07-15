import yaml
import os
import glob

class Base:
    def __init__(self, path):
        self.path = path
    def load_config_path(self):
        return glob.glob(os.path.join(self.path, "*.yml"))
    def load_file(self, path):
        with open(path, "r") as f:
            return yaml.safe_load(f)