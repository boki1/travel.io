import os
import unittest

import openai
from dotenv import load_dotenv
from flask import Flask, jsonify, request
from pyairports.airports import Airports

from scripts.analyzer import Analyser, TestAnalyzer
from scripts.config import *
from scripts.config import g_analyser_options
from scripts.openai.ctl import OpenAIController
from scripts.task import Task

# This line loads the values from the .env file into the environment
load_dotenv(dotenv_path=os.path.join(os.path.dirname(__file__), '../props.env'))

app = Flask(__name__)
openai.api_key = os.getenv('OPENAI_SECRET_KEY', 'default-secret-key')


g_airports = Airports().airports

@app.route('/api/v1/analyzer', methods=['POST'])
def analyze_inp_route():
    question = request.get_data(as_text=True)
    answer = OpenAIController.ask(question)
    if '' in (question, answer):
        return jsonify({'error': f'question or answer is empty (question="{question}, answer="{answer}"'})

    task = Task(question, answer)
    analyser = Analyser(g_analyser_options)
    output = analyser.perform(task)
    return jsonify(output)


@app.route('/api/v1/airports', methods=['POST'])
def airport_iata_route():
    data = request.get_json()
    airport_city = data['city']
    airport_country = data['country']

    selected_airports = set()
    for _, airport_obj in g_airports.items():
        if airport_obj.city == airport_city and airport_obj.country == airport_country:
            selected_airports.add(airport_obj)
    if not len(selected_airports):
        return jsonify({'error': f'no viable suggestion in provided city, country ({airport_city}, {airport_country})'})
    selected_airport = list(selected_airports)[0]
    return jsonify(selected_airport._asdict())


if DEBUG_MODE:
    def testsuite():
        suite = unittest.TestSuite()
        suite.addTest(TestAnalyzer('test_samples'))
        return suite


    runner = unittest.TextTestRunner()
    runner.run(testsuite())
