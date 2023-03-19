import re
import unittest
from os import listdir

from countryinfo import CountryInfo
from geopy.geocoders import Nominatim

from scripts.config import debug_print
from scripts.task import *


class Analyser:

    def __init__(self, options: dict):
        self.geolocator = Nominatim(user_agent="my_app")
        self.options = options

    @staticmethod
    def _country_from_full_location(location) -> str:
        # FIXME: Ugly access to country field.
        return location.raw.get('display_name').split(',')[-1].strip()

    # Locates the country that a given city resides in.
    # Returns a list with possibilities.
    def _city_to_countries(self, city: str) -> list[str]:
        locations = self.geolocator.geocode(city, exactly_one=False, language="en")
        country_names = list({Analyser._country_from_full_location(loc) for loc in locations})
        debug_print(f'_city_to_countries: country_names "{country_names}"')
        return country_names

    @staticmethod
    def _produce_city(country: str):
        return CountryInfo(country).capital()

    def get_locations_serial(self, task: Task) -> list[Location]:
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
        locations = self.get_locations_serial(task)
        debug_print(f'Locations: "{locations}"')
        action_topics = []
        landmarks = []

        # TODO: Populate with data :). Working on it...
        out = Out(locations, action_topics, landmarks)
        return out


# TODO: Move to travel.io/test/feature/
class TestAnalyzer(unittest.TestCase):

    def setUp(self):
        global g_analyser_options
        self.a = Analyser(g_analyser_options)

    def test_city_to_country_list(self):
        expected = 'Bulgaria'
        countries_that_contain_sofia = self.a._city_to_countries('Sofia')
        debug_print(f'countries_that_contain_sofia = "{countries_that_contain_sofia}"')
        self.assertTrue(expected in countries_that_contain_sofia)

        expected = 'France'
        countries_that_contain_basque_region = self.a._city_to_countries('Basque')
        debug_print(f'countries_that_contain_basque = "{countries_that_contain_basque_region}"')
        self.assertTrue(expected in countries_that_contain_basque_region)

        expected = 'Germany'
        countries_that_contain_berlin = self.a._city_to_countries('Berlin')
        debug_print(f'countries_that_contain_berlin = "{countries_that_contain_berlin}"')
        self.assertTrue(expected in countries_that_contain_berlin)

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

    def test_france_task(self):
        fpath = '../../../test/inputs/france_beach.txt'
        task = TestAnalyzer.create_task_from_desc(fpath)
        out = self.a.perform(task)
        expected_location = Location(city='Basque', country='France')
        self.assertEqual(out.locations[0], expected_location)

    def test_perform_sample_tasks(self):
        test_path = '../../../test/inputs'
        for fname in listdir(test_path):
            task = TestAnalyzer.create_task_from_desc(f'{test_path}/{fname}')
            output = self.a.perform(task)
            print(f"For input: '{fname}' output is '{output}'")

    # Figure out how to use this library's API
    def test_country_to_name_basic_lib(self):
        geolocator = Nominatim(user_agent="my_app")
        cities = {"Paris": "France", "Sofia": "Bulgaria", "Basque": "France"}
        for city, expected_country in cities.items():
            locations = geolocator.geocode(city, exactly_one=False, language="en")
            country_names = list({Analyser._country_from_full_location(loc) for loc in locations})
            self.assertTrue(expected_country in country_names)

    def test_produce_country(self):
        self.assertEqual(Analyser._produce_city("Bulgaria"), "Sofia")
        self.assertEqual(Analyser._produce_city("Cambodia"), "Phnom Penh")
        self.assertNotEqual(Analyser._produce_city("Greece"), "Gondor")

    def test_only_country_task(self):
        fpath = '../../test/inputs/only_country.txt'
        task = TestAnalyzer.create_task_from_desc(fpath)
        out = self.a.perform(task)
        expected_location = Location(city='Sofia', country='Bulgaria')
        self.assertEqual(out.locations[0], expected_location)
