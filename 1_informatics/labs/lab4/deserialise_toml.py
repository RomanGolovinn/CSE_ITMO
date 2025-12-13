def _format_value(value):
    if isinstance(value, str):
        return f'"{value}"'
    elif isinstance(value, bool):
        return str(value).lower()
    elif isinstance(value, (int, float)):
        return str(value)
    elif isinstance(value, list):
        elements = [f'"{e}"' if isinstance(e, str) else str(e) for e in value]
        return f'[{", ".join(elements)}]'
    elif value is None:
        return '""'
    

def convert_to_toml(data: dict) -> str:
    toml_output = ""
    
    def serialize_recursive(current_data, prefix=""):
        nonlocal toml_output
        
        simple_keys = {}
        table_keys = {}
        array_of_tables_keys = {}

        for key, value in current_data.items():
            if isinstance(value, dict):
                table_keys[key] = value
            elif isinstance(value, list) and all(isinstance(item, dict) for item in value):
                array_of_tables_keys[key] = value
            else:
                simple_keys[key] = value

        for key, value in simple_keys.items():
            toml_output += f'{key} = {_format_value(value)}\n'
        
        for key, value in table_keys.items():
            new_prefix = f'{prefix}.{key}' if prefix else key
            toml_output += f'\n\n[{new_prefix}]\n'
            serialize_recursive(value, new_prefix)
            
        for key, array_items in array_of_tables_keys.items():
            new_prefix = f'{prefix}.{key}' if prefix else key
            
            for item in array_items:
                toml_output += f'\n\n[[{new_prefix}]]\n'
                serialize_recursive(item, new_prefix)
                
    serialize_recursive(data)
    
    return toml_output

def demonstration_toml():
   
    toml_output = convert_to_toml(MOCK_INTERNAL_OBJECT)
    


if __name__ == '__main__':
    demonstration_toml()