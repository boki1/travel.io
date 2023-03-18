import os

import requests
from flask import json, request, jsonify


class OpenAICommunication:
    def __init__(self, app):
        self.app = app

    def run(self):
        self.app.run(host=os.getenv('FLASK_RUN_HOST', '0.0.0.0'), port=os.getenv('FLASK_RUN_PORT', 5005))

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

        return question, answer
