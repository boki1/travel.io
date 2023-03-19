import os

import requests
from flask import json, request
from scripts.config import g_openai_hints


class OpenAICommunication:
    def __init__(self, app):
        self.app = app

    def run(self):
        self.app.run(host=os.getenv('FLASK_RUN_HOST', '0.0.0.0'), port=os.getenv('FLASK_RUN_PORT', 5005))

    @staticmethod
    def hint():
        global g_openai_hints
        return \
            """
            Provide the data, formatted in an XML-fashion: <LOCATION>Country, City</LOCATION> <DESCRIPTION>...</DESCRIPTION>.
            In the description mark each landmark, town, facilities or other specific locations and establishments enclosed
            in a XML-like tag <LNDMARK> and each vacation activity verb phrase with <ACTIVITY>. Make sure to put corresponding
            closing tags. Also wrap each <LOCATION>-<DESCRIPTION> pair in a <DESTINATION> tag.
            """

    def get_openai_answer(self, question):
        if not question:
            return None

        openai_api_key = os.getenv('OPENAI_SECRET_KEY', 'default-secret-key')

        headers = {
            "Content-Type": "application/json",
            "Authorization": f"Bearer {openai_api_key}",
        }

        # Hint OpenAI in order to omit unnecessary data processing of the response.
        question += OpenAICommunication.hint()

        data = {
            "model": "gpt-3.5-turbo",
            "messages": [{"role": "user", "content": question}],
            "temperature": 0.1,
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

        answer = self.get_openai_answer(question)

        if not answer or not question:
            return "", ""

        return question, answer
