import typing


class Location(typing.NamedTuple):
    city: str
    country: str


class Out(typing.NamedTuple):
    locations: list[Location]
    action_topics: list[str]
    landmarks: list[str]

    def full(self):
        # FIXME: Make JSON nicer. This looks kind of bad :/
        return self._asdict()


class Task:
    extracted_locations: list[Location]

    def __init__(self, question, full_answer):
        self.inp_question = question
        self.inp_answer = full_answer

        # These will get puplated by the analyser at some point via populate().
        self.inp_answer_keywords = None

    def populate(self):
        pass
