DEBUG_MODE = True

# Configuration passed to the Analyser ctor at init.
g_analyser_options = {
    'nominatim_language': 'en'
}

# Useful utilities not related to real functionality.

def debug_print(msg):
    if DEBUG_MODE:
        print(f"MYDEBUG: {msg}")
