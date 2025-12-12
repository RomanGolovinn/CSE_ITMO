import json
import toml

def f(content):
        internal_data_object = json.loads(content)
        toml_string = toml.dumps(internal_data_object)
        return toml_string

if __name__ == "__main__":
        with open('schedule.json', 'r', encoding='utf-8') as file:
                content = file.read()

        print(content, "\n")
        s = f(content)
        print(s)
