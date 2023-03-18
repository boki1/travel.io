import os
from os import listdir

import os
from os import listdir

import openai
from dotenv import load_dotenv
from flask import Flask, jsonify

from scripts.analyzer import Analyser
from scripts.config import *
from scripts.config import g_analyser_options
from scripts.openai.communication import OpenAICommunication
from scripts.task import Task


class TaskCreator:
    @staticmethod
    def create_task_from_desc(fname_path) -> Task:
        t = Task('', '')  # dummy construction
        with open(fname_path, 'r') as mock_task_desc:
            for line in mock_task_desc:
                if line.startswith('--'):
                    t.inp_question += line.replace('-- ', '')
                else:
                    t.inp_answer += line
        return t


def debug_main():
    task_creator = TaskCreator()
    analyser = Analyser(g_analyser_options)

    test_path = '../../../test/inputs'
    for fname in listdir(test_path):
        task = task_creator.create_task_from_desc(f'{test_path}/{fname}')
        output = analyser.perform(task)
        print(f"For input: '{fname}' output is '{output}'")


# This line loads the values from the .env file into the environment
load_dotenv(dotenv_path=os.path.join(os.path.dirname(__file__), '../props.env'))

app = Flask(__name__)
openai.api_key = os.getenv('OPENAI_SECRET_KEY', 'default-secret-key')

openai_communication = OpenAICommunication(app)


@app.route('/api/v1/analyzer', methods=['POST'])
def openai_route():
    question, answer = openai_communication.handle_request()
    task = Task(question, answer)
    analyser = Analyser(g_analyser_options)
    output = analyser.perform(task)
    return jsonify(output.full())


if DEBUG_MODE:
    debug_main()
elif __name__ == '__main__':
    openai_communication.run()
