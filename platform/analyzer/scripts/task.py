import typing


class Location(typing.NamedTuple):
    city: str
    country: str
    description: str


class Activity(typing.NamedTuple):
    name: str
    location: Location


class Landmark(typing.NamedTuple):
    name: str
    location: Location


class Out(typing.NamedTuple):
    destinations: dict


class Task:
    extracted_locations: list[Location]

    def __init__(self, question, full_answer):
        self.inp_question = question
        self.inp_answer = full_answer

        # These will get puplated by the analyser at some point via populate().
        self.inp_answer_keywords = None

    def populate(self):
        pass
