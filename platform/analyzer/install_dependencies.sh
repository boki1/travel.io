#!/bin/bash

# Install Python packages from requirements.txt
pip install -r requirements.txt

# Download the spaCy model
python -m spacy download en_core_web_sm
