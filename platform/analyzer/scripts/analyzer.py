import re
import unittest
from os import listdir

from countryinfo import CountryInfo
from geopy.geocoders import Nominatim
from bs4 import BeautifulSoup as bs

from scripts.config import debug_print, g_openai_hints, g_analyser_options, hint_marker
from scripts.task import *


class Analyser:

    def __init__(self, options: dict):
        self.options = options

    def extract_locations(self, task: Task) -> list[Location]:
        location_list = []
        suggestions = task.inp_answer.strip().split('\n')

        for suggestion in suggestions:
            if not suggestion.strip():
                continue  # Ignore empty lines

            suggestion_data = re.sub(r'^\d+\. ', '', suggestion)

            # Remove the index number
            suggestion_data_result = suggestion_data.split(': ', 1)
            if len(suggestion_data_result) != 2:
                continue
            city_country, _ = suggestion_data_result

            city_country_result = city_country.split(', ', 1)
            if len(city_country_result) != 2:
                continue
            city, country = city_country_result

            location_list.append(Location(city.strip(), country.strip()))

        task.extracted_locations = location_list
        return location_list

    def perform(self, task: Task) -> Out:
        locations = self.extract_locations(task)
        soup = bs(task.inp_answer, features="lxml")
        landmark_marker = hint_marker(self.options['openai_hints']['landmark_marker'])
        activity_marker = hint_marker(self.options['openai_hints']['activity_marker'])
        landmarks = [lm_obj.text for lm_obj in soup.find_all(landmark_marker)]
        activities = [act_obj.text for act_obj in soup.find_all(activity_marker)]
        out = Out(locations, activities, landmarks)
        return out


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
            out = self.analyzer.perform(sample_task)
            debug_print(f'Locations: [{out.locations}]')
            debug_print(f'Landmarks: [{out.landmarks}]')
            debug_print(f'Activities: [{out.action_topics}]')
            return True  # :)

        for sample_task in TestAnalyzer.for_each_sample():
            self.assertTrue(test_sample(sample_task))
