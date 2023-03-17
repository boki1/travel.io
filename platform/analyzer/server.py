from os import listdir
from config import *
from analyzer import *

def debug_main():
    def create_task_from_desc(fname_path) -> Task:
        t = Task('', '') # dummy construction
        with open(fname_path, 'r') as mock_task_desc:
            for line in mock_task_desc:
                if line.startswith('--'):
                    t.inp_question += line.replace('-- ', '')
                else:
                    t.inp_answer += line
        return t

    global g_analyser_options
    analyser = Analyser(g_analyser_options)

    test_path='../../test/inputs'
    for fname in listdir(test_path):
        task = create_task_from_desc(f'{test_path}/{fname}')
        output = analyser.perform(task)


# We are not called this way usually. The analyzer module is suppossed to be
# used by API from the "flask" server. However in order to mock the stages that
# usually lead to calling the analyzer module, this is a _debug main_. It skips
# a couple of parts of the overall process such as invoking the OpenAI API,
# construcing a task and communicating it to the Redis backup log.

# TODO: Implement a flask endpoint here.


if DEBUG_MODE:
    debug_main()
