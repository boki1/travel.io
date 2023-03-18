DEBUG_MODE = True

# Configuration passed to the Analyser ctor at init.
g_analyser_options = {
    'nominatim_language': 'en'
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