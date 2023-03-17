from os import listdir
import json
import os
from flask import Flask, request, jsonify
import openai
from dotenv import load_dotenv

from config import *
from analyzer import *

def debug_main():
    def create_task_from_desc(fname_path) -> Task:
        t = Task('', '') # dummy construction
        with open(fname_path, 'r') as mock_task_desc:
            for line in mock_task_desc:
                if line.startswith('--'):
                    t.inp_question += line.replace('-- ', '')
                else:
                    t.inp_answer += line
        return t

    global g_analyser_options
    analyser = Analyser(g_analyser_options)

    test_path='../../test/inputs'
    for fname in listdir(test_path):
        task = create_task_from_desc(f'{test_path}/{fname}')
        output = analyser.perform(task)


# We are not called this way usually. The analyzer module is suppossed to be
# used by API from the "flask" server. However in order to mock the stages that
# usually lead to calling the analyzer module, this is a _debug main_. It skips
# a couple of parts of the overall process such as invoking the OpenAI API,
# construcing a task and communicating it to the Redis backup log.

app = Flask(__name__)
openai.api_key = os.getenv('OPENAI_SECRET_KEY', 'default-secret-key')

# FIXME: Dummy entrypoint
@app.route('/travelerio/api/v1.0/hello', methods=['GET'])
def openai_communication():
    return jsonify({'message': os.getenv('OPENAI_SECRET_KEY', 'default-secret-key')})

if DEBUG_MODE:
    debug_main()

if __name__ == '__main__':
    # This line loads the values from the .env file into the environment
    load_dotenv(dotenv_path=os.path.join(os.path.dirname(__file__), 'props.env'))

    app.run()
