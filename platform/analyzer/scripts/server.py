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
    def suite():
        suite = unittest.TestSuite()
        suite.addTest(TestAnalyzer('test_city_to_country_list'))
        suite.addTest(TestAnalyzer('test_france_task'))
        suite.addTest(TestAnalyzer('test_country_to_name_basic_lib'))
        return suite

    runner = unittest.TextTestRunner()
    runner.run(suite())

elif __name__ == '__main__':
    openai_communication.run()
