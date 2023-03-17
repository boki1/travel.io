import os
import subprocess

workers = os.cpu_count()
threads = 2  # TODO: undergoing customization - probably increase?
subprocess.run(["gunicorn", "-w", str(workers), "--threads", str(threads), "app:app"])