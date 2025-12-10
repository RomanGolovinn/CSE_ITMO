import json
import toml

with open('schedule.json', 'r', encoding='utf-8') as file:
        content = file.read()

print(content, "\n")
internal_data_object = json.loads(content)
toml_string = toml.dumps(data)
print(toml_string)