import json
import os
from flask import Flask, request, jsonify
import openai
from dotenv import load_dotenv

# This line loads the values from the .env file into the environment
load_dotenv(
    dotenv_path=os.path.join(os.path.dirname(__file__), 'props.env')
)

app = Flask(__name__)
openai.api_key = os.getenv('OPENAI_SECRET_KEY', 'default-secret-key')


@app.route('/travelerio/api/v1.0/hello', methods=['GET'])
def openai_communication():
    # for test purposes
    return jsonify({'message': os.getenv('OPENAI_SECRET_KEY', 'default-secret-key')})


if __name__ == '__main__':
    app.run()
