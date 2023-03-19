DEBUG_MODE = False

g_openai_hints = {
    'location_fmt': '<LOCATION>Country, City</LOCATION> Description.',
    'landmark_marker': '<LNDMARK>',
    'activity_marker': '<ACTIVITY>',
    'location_marker': '<LOCATION>',
    'destination_marker': '<DESTINATION>',
    'description_marker': '<DESCRIPTION>',
}


def hint_marker(m):
    return m.strip('<>').lower()


# Configuration passed to the Analyser ctor at init.
g_analyser_options = {
    'openai_hints': g_openai_hints
}


# Useful utilities not related to real functionality.

def debug_print(msg):
    if DEBUG_MODE:
        print(f"MYDEBUG: {msg}")


def debug_assert(expr, msg):
    if not DEBUG_MODE:
        return
    if not expr:
        print(f"FAIL: {msg}")
        assert expr