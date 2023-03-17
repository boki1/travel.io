from collections import namedtuple
import typing

class Location(typing.NamedTuple):
    city: str
    country: str

class ActionTopic(typing.NamedTuple):
    location: Location
    topic: str
    # TODO: Maybe hardcode an enumeration for this value? e.g enum { sport, art, music, ... }
    category: str

class Landmark(typing.NamedTuple):
    name: str
    location: Location

class Out(typing.NamedTuple):
    locations: list[Location]
    action_topics: list[ActionTopic]
    landmarks: list[Landmark]


class Task:

    def __init__(self, question, full_answer):
        self.inp_question = question
        self.inp_answer = full_answer

        # These will get puplated by the analyser at some point via populate().
        self.inp_answer_keywords = None

    def populate(self):
        pass


