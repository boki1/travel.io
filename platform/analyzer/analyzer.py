from task import *

class Analyser:

    def __init__(self, options: dict):
        self.options = options

    def perform(self, task) -> Out:
        locations = []
        action_topics = []
        landmarks = []

        # TODO: Populate with data :).
        out = Out(locations, action_topics, landmarks)
        return out
