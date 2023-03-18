import json
import os
from os import listdir

import openai
import requests
from dotenv import load_dotenv
from flask import Flask, request, jsonify

from analyzer import *
from config import *


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


class OpenAICommunication:
    def __init__(self, app):
        self.app = app

    def run(self):
        self.app.run(host=os.getenv('FLASK_RUN_HOST', '0.0.0.0'), port=os.getenv('FLASK_RUN_PORT', 5005))

    @staticmethod
    def analyze_question_and_answer(question, answer):
        task = Task(question, answer)
        analyser = Analyser(g_analyser_options)
        output = analyser.perform(task)
        return output

    def get_openai_answer(self, question):
        openai_api_key = os.getenv('OPENAI_SECRET_KEY', 'default-secret-key')

        headers = {
            "Content-Type": "application/json",
            "Authorization": f"Bearer {openai_api_key}",
        }

        data = {
            "model": "gpt-3.5-turbo",
            "messages": [{"role": "user", "content": question}],
            "temperature": 0.7,
        }

        response = requests.post(
            "https://api.openai.com/v1/chat/completions",
            headers=headers,
            data=json.dumps(data),
        )

        if response.status_code == 200:
            response_data = response.json()
            answer = response_data["choices"][0]["message"]["content"]
            return answer

        return None

    def handle_request(self):
        question = request.get_data(as_text=True)
        if not question:
            return jsonify({"error": "No input data provided"}), 400

        answer = self.get_openai_answer(question)
        if answer is None:
            return jsonify({"error": "Unable to communicate with OpenAI API"}), 400

        output = self.analyze_question_and_answer(question, answer)
        return jsonify(output.full())


def debug_main():
    task_creator = TaskCreator()
    analyser = Analyser(g_analyser_options)

    test_path = '../../test/inputs'
    for fname in listdir(test_path):
        task = task_creator.create_task_from_desc(f'{test_path}/{fname}')
        output = analyser.perform(task)
        print(f"For input: '{fname}' output is '{output}'")


# This line loads the values from the .env file into the environment
load_dotenv(dotenv_path=os.path.join(os.path.dirname(__file__), 'props.env'))

app = Flask(__name__)
openai.api_key = os.getenv('OPENAI_SECRET_KEY', 'default-secret-key')

openai_communication = OpenAICommunication(app)


@app.route('/api/v1/analyzer', methods=['POST'])
def openai_route():
    return openai_communication.handle_request()


if DEBUG_MODE:
    debug_main()
elif __name__ == '__main__':
    openai_communication.run()
