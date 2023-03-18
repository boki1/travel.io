import os
import subprocess

from dotenv import load_dotenv

load_dotenv(dotenv_path=os.path.join(os.path.dirname(__file__), 'props.env'))

workers = os.cpu_count()
threads = 2  # TODO: undergoing customization - probably increase?
subprocess.run(["gunicorn", "-w", str(workers),
                "--threads", str(threads),
                "-b", os.getenv('FLASK_RUN_HOST_PORT', '0.0.0.0:5005'),
                "server:app"])
