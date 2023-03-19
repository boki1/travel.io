import re
import unittest
from os import listdir
import re

from countryinfo import CountryInfo
from geopy.geocoders import Nominatim
from bs4 import BeautifulSoup as bs

from scripts.config import debug_print, g_openai_hints, g_analyser_options, hint_marker
from scripts.task import *


class Analyser:

    def __init__(self, options: dict):
        self.options = options

    @staticmethod
    def cleanup_description(description_raw: str) -> str:
        pattern = r'<.*?>'
        return re.sub(pattern, '', description_raw)

    def perform(self, task: Task) -> Out:
        raw = self.extract(task)
        out = self.prepare_out(raw)
        return out

    def prepare_out(self, destinations):
        locations = []
        activities = []
        landmarks = []
        for dest in destinations:
            country, city = dest['location'].split(', ')
            description = self.cleanup_description(dest['description'].text).strip(' \n')
            loc = Location(city, country, description)
            locations.append(loc)
            for landmark in dest['landmarks']:
                lm = Landmark(name=landmark, location=loc)
            landmarks.append(lm)
            for activity in dest['activities']:
                act = Activity(name=activity, location=loc)
            activities.append(act)
        out = Out(locations, activities, landmarks)
        return out

    def extract(self, task: Task) -> Out:
        soup = bs(task.inp_answer, features='html.parser')
        destination_tags = soup.find_all('destination')
        destinations = []
        for destination in destination_tags:
            location = destination.find('location').text
            description = destination.find('description')
            landmarks = [lndmark.text for lndmark in description.find_all('lndmark')]
            activities = [activity.text for activity in description.find_all('activity')]
            destination_info = {
                'location': location,
                'landmarks': landmarks,
                'activities': activities,
                'description': description
            }
            destinations.append(destination_info)
        return destinations


# TODO: Move to travel.io/test/feature/
class TestAnalyzer(unittest.TestCase):

    def setUp(self):
        global g_analyser_options
        self.analyzer = Analyser(g_analyser_options)

    @staticmethod
    def create_task_from_desc(fname_path: str) -> Task:
        t = Task('', '')  # dummy construction
        with open(fname_path, 'r') as mock_task_desc:
            for line in mock_task_desc:
                if line.startswith('--'):
                    t.inp_question += line.replace('-- ', '')
                else:
                    t.inp_answer += line
        return t

    @staticmethod
    def for_each_sample():
        samples_path = '../../../test/inputs/'
        for sample_path in listdir(samples_path):
            sample_task = TestAnalyzer.create_task_from_desc(f'{samples_path}/{sample_path}')
            yield sample_task

    def test_samples(self):
        def test_sample(sample_task) -> bool:
            out = self.analyzer.extract(sample_task)
            debug_print(f'Locations: [{out.locations}]')
            debug_print(f'Landmarks: [{out.landmarks}]')
            debug_print(f'Activities: [{out.activities}]')
            return True  # :)

        for sample_task in TestAnalyzer.for_each_sample():
            self.assertTrue(test_sample(sample_task))

    def test_parsing(self):
        test_path = '../../../test/inputs/france_beach.txt'
        sample = TestAnalyzer.create_task_from_desc(test_path)
        out = self.analyzer.perform(sample)
        print(out)
