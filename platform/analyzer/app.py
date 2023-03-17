import json
from time import sleep

from flask import Flask, request, jsonify
import openai

app = Flask(__name__)

a = 0


@app.route('/travelerio/api/v1.0/hello', methods=['GET'])
def openai_communication():  # put application's code here
    global a
    a += 1
    sleep(3)
    return jsonify({'message': str(a)})


if __name__ == '__main__':
    app.run()
