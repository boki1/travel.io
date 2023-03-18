import spacy

from task import *


class Analyser:

    def __init__(self, options: dict):
        self.options = options

    def get_locations(self, task: Task):
        nlp = spacy.load("en_core_web_sm")
        doc = nlp(task.inp_answer)
        locations = list(set([str(ent) for ent in doc.ents if ent.label_ == 'GPE']))

        # FIXME: Fill in both country and city.
        locations = [Location('', c) for c in locations]
        return locations

    def perform(self, task: Task) -> Out:
        locations = self.get_locations(task)
        action_topics = []
        landmarks = []

        # TODO: Populate with data :). Working on it...
        out = Out(locations, action_topics, landmarks)
        return out
