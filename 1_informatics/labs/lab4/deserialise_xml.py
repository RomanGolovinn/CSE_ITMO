from serialise_json import *

with open('schedule.json', 'r', encoding='utf-8') as file:
        content = file.read()

parser = JSONParser(content)
MOCK_INTERNAL_OBJECT = parser.parse()

def to_xml_recursive(data, indent=""):
    """
    Рекурсивно преобразует словарь или список Python в форматированную XML-строку 
    """
    xml_string = ""
    
    if isinstance(data, dict):
        for key, value in data.items():
            tag = key.replace('.', '_')
            
            if isinstance(value, list):
                item_tag = tag[:-1] if tag.endswith('s') else "item"
                xml_string += f"{indent}<{tag}>\n"
                
                
                for item in value:
                    if isinstance(item, dict):
                        xml_string += f"{indent}  <{item_tag}>\n"
                        xml_string += to_xml_recursive(item, indent + "    ")
                        xml_string += f"{indent}  </{item_tag}>\n"
                    elif not isinstance(item, list):
                        xml_string += f"{indent}  <{item_tag}>{item}</{item_tag}>\n"
                        
                xml_string += f"{indent}</{tag}>\n"
                
            elif isinstance(value, dict):
                xml_string += f"{indent}<{tag}>\n"
                xml_string += to_xml_recursive(value, indent + "  ")
                xml_string += f"{indent}</{tag}>\n"
            else:
                xml_string += f"{indent}<{tag}>{value}</{tag}>\n"
                
    return xml_string


def convert_to_xml(data: dict) -> str:
    """
    Сериализует словарь Python в строку XML
    """
    xml_output = '<?xml version="1.0" encoding="utf-8"?>\n'
    
    xml_output += '<schedule>\n'
    
    xml_output += to_xml_recursive(data, indent="  ")
    
    xml_output += '</schedule>\n'
    
    return xml_output


def demonstration_xml():
    xml_output = convert_to_xml(MOCK_INTERNAL_OBJECT)
    print(xml_output)



if __name__ == '__main__':
    demonstration_xml()