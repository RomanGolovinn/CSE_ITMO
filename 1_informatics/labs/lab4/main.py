from serialise_json import *
from deserialise_toml import *

with open('schedule.json', 'r', encoding='utf-8') as file:
        content = file.read()
parser = JSONParser(content)
internal_data_object = parser.parse()
data = convert_to_toml(internal_data_object)

with open('schedule.toml', 'w', encoding='utf-8') as file:
        file.write(data)