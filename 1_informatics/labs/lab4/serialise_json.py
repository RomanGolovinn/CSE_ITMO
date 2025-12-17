class JSONParser:
    def __init__(self, text):
        self.text = text
        self.index = 0

    def skip_space(self):
        """Продвигает указатель self.index, пропуская все пробельные символы."""
        while self.index < len(self.text) and self.text[self.index].isspace():
            self.index += 1

    def get_next_char(self):
        """Возвращает следующий символ и увеличивает индекс, не пропуская пробелы."""
        if self.index < len(self.text):
            char = self.text[self.index]
            self.index += 1
            return char
        return ''

    def peek_char(self):
        """Возвращает следующий токен (не пробел) без увеличения индекса."""
        self.skip_space()
        if self.index < len(self.text):
            return self.text[self.index]
        return ''

    def parse_char(self, char):
        """Проверяет, что следующий токен соответствует ожидаемому, и пропускает его."""
        self.skip_space()
        actual_char = self.get_next_char()
        if actual_char != char:
            raise SyntaxError(f"Ожидался символ '{char}', но найден '{actual_char}' в позиции {self.index - 1}")

    def parse_object(self):
        """Парсит JSON-объект ({...})."""
        self.parse_char('{')
        data = {}

        if self.peek_char() == '}':
            self.parse_char('}')
            return data

        while True:
            self.skip_space()
            key = self.parse_string() 
            
            self.parse_char(':')
            
            value = self.parse_value()
            data[key] = value

            if self.peek_char() == '}':
                self.parse_char('}')
                break

            self.parse_char(',')
        
        return data

    def parse_array(self):
        """Парсит JSON-массив ([...]) и возвращает список Python."""
        self.parse_char('[')
        elements = []

        if self.peek_char() == ']':
            self.parse_char(']')
            return elements

        while True:
            value = self.parse_value()
            elements.append(value)

            if self.peek_char() == ']':
                self.parse_char(']')
                break

            self.parse_char(',')
            
        return elements

    def parse_string(self):
        """
        Парсит JSON-строку ("..."). 
        """
        self.parse_char('"')
        start_index = self.index

        while self.index < len(self.text) and self.text[self.index] != '"':
            self.get_next_char()
        
        string_value = self.text[start_index:self.index]
        self.parse_char('"')
        return string_value

    def parse_number(self):
        """Парсит число"""
        start_index = self.index

        if self.text[self.index] == '-':
            self.get_next_char()

        while self.index < len(self.text) and '0' <= self.text[self.index] <= '9':
            self.get_next_char()
            
        if self.index < len(self.text) and self.text[self.index] == '.':
            self.get_next_char()
            while self.index < len(self.text) and '0' <= self.text[self.index] <= '9':
                self.get_next_char()
        
        if self.index == start_index:
             raise SyntaxError(f"Ожидалось число в позиции {start_index}, но ничего не найдено.")
             
        number_str = self.text[start_index:self.index]
        
        if '.' in number_str:
            return float(number_str)
        return int(number_str)

    def parse_value(self):
        """Определяет тип следующего значения и вызывает соответствующий парсер."""
        self.skip_space()
        char = self.peek_char()
        
        if char == '{':
            return self.parse_object()
        elif char == '[':
            return self.parse_array()
        elif char == '"':
            return self.parse_string()
        elif char in '0123456789-':
            self.get_next_char()
            self.index -= 1
            return self.parse_number() 
        elif char == 't':
            self.parse_char('t')
            self.parse_char('r')
            self.parse_char('u')
            self.parse_char('e')
            return True
        elif char == 'f':
            self.parse_char('f')
            self.parse_char('a')
            self.parse_char('l')
            self.parse_char('s')
            self.parse_char('e')
            return False
        elif char == 'n':
            self.parse_char('n')
            self.parse_char('u')
            self.parse_char('l')
            self.parse_char('l')
            return None
        else:
            raise SyntaxError(f"Неизвестный тип значения в позиции {self.index}")
            
    def parse(self):
        """Начинает парсинг, ожидая корневой объект."""
        result = self.parse_object()
        self.skip_space()
        if self.index != len(self.text):
            print(f"Предупреждение: Парсинг завершился, но остались неразобранные символы в позиции {self.index}")
        return result



def run_lab_work():
    with open('schedule.json', 'r', encoding='utf-8') as file:
        content = file.read()
    parser = JSONParser(content)
    internal_data_object = parser.parse()

    return internal_data_object

if __name__ == '__main__':
    data = run_lab_work()
    print(data)
    
    with open('schedule.bin', 'w', encoding='utf-8') as file:
        file.write(str(data))