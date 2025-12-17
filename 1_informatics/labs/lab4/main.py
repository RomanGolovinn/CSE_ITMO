from serialise_json import *
from deserialise_toml import *

def convert():
        with open('schedule.json', 'r', encoding='utf-8') as file:
                content = file.read()
        parser = JSONParser(content)
        internal_data_object = parser.parse()
        print("json serialised")
        print(internal_data_object)
        data = convert_to_toml(internal_data_object)
        print(data)
        print("toml deserialised")

        with open('schedule.toml', 'w', encoding='utf-8') as file:
                file.write(data)

if __name__ == "__main__":
        convert()