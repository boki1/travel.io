import os
import unittest

import openai
from dotenv import load_dotenv
from flask import Flask, jsonify, request
from pyairports.airports import Airports

from scripts.analyzer import Analyser, TestAnalyzer
from scripts.config import *
from scripts.config import g_analyser_options
from scripts.openai.communication import OpenAICommunication
from scripts.task import Task

# This line loads the values from the .env file into the environment
load_dotenv(dotenv_path=os.path.join(os.path.dirname(__file__), '../props.env'))

app = Flask(__name__)
openai.api_key = os.getenv('OPENAI_SECRET_KEY', 'default-secret-key')

openai_communication = OpenAICommunication(app)

g_airports = Airports().airports

@app.route('/api/v1/analyzer', methods=['POST'])
def openai_route():
    question, answer = openai_communication.handle_request()

    if question is None or answer is None:
        return jsonify({'error': 'question or answer is empty'})

    task = Task(question, answer)
    analyser = Analyser(g_analyser_options)
    output = analyser.perform(task)
    return jsonify(output)


@app.route('/api/v1/airports', methods=['POST'])
def airport_iata_route():
    data = request.get_json()
    airport_city = data['city']
    airport_country = data['country']

    filtered_airports = {iata_code: airport_obj for iata_code, airport_obj in g_airports.items() \
                         if airport_obj.city == airport_city and airport_obj.country == airport_country}

    if len(filtered_airports) > 0:
        return list(filtered_airports.keys())[0]
    return ''


if DEBUG_MODE:
    def testsuite():
        suite = unittest.TestSuite()
        suite.addTest(TestAnalyzer('test_samples'))
        return suite


    runner = unittest.TextTestRunner()
    runner.run(testsuite())
