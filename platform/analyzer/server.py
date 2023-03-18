import json
import os
from os import listdir

import openai
import requests
from dotenv import load_dotenv
from flask import Flask, request, jsonify

from analyzer import *
from config import *


def debug_main():
    def create_task_from_desc(fname_path) -> Task:
        t = Task('', '')  # dummy construction
        with open(fname_path, 'r') as mock_task_desc:
            for line in mock_task_desc:
                if line.startswith('--'):
                    t.inp_question += line.replace('-- ', '')
                else:
                    t.inp_answer += line
        return t

    global g_analyser_options
    analyser = Analyser(g_analyser_options)

    test_path = '../../test/inputs'
    for fname in listdir(test_path):
        task = create_task_from_desc(f'{test_path}/{fname}')
        output = analyser.perform(task)
        print(f"For input: '{fname}' output is '{output}'")


# We are not called this way usually. The analyzer module is supposed to be
# used by API from the "flask" server. However, in order to mock the stages that
# usually lead to calling the analyzer module, this is a _debug main_. It skips
# a couple of parts of the overall process such as invoking the OpenAI API,
# constructing a task and communicating it to the Redis backup log.

# This line loads the values from the .env file into the environment
load_dotenv(dotenv_path=os.path.join(os.path.dirname(__file__), 'props.env'))

app = Flask(__name__)
openai.api_key = os.getenv('OPENAI_SECRET_KEY', 'default-secret-key')


@app.route('/api/v1/analyzer', methods=['POST'])
def openai_communication():
    openai_api_key = os.getenv('OPENAI_SECRET_KEY', 'default-secret-key')

    headers = {
        "Content-Type": "application/json",
        "Authorization": f"Bearer {openai_api_key}",
    }

    # Get input data from the client request
    question = request.get_data(as_text=True)
    if not question:
        return jsonify({"error": "No input data provided"}), 400

    # Use question as the messages for the API request
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
        # FIXME: rationalize this
        response_data = response.json()
        answer = response_data["choices"][0]["message"]["content"]
        task = Task(question, answer)
        analyser = Analyser(g_analyser_options)
        output = analyser.perform(task)
        return jsonify(output.full())
    else:
        return jsonify({"error": "Unable to communicate with OpenAI API"}), response.status_code


if DEBUG_MODE:
    debug_main()
elif __name__ == '__main__':
    # To make the Flask server accessible from other devices on the same local network,
    # it needs to be bound to a public IP address (0.0.0.0) instead of the loopback address (127.0.0.1).
    app.run(host=os.getenv('FLASK_RUN_HOST', '0.0.0.0'), port=os.getenv('FLASK_RUN_PORT', 5005))
