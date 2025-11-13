import re
import unittest

def checkText(text : str):
    fragments = []
    for k in range(0, 5):
        pat = rf'(\bВТ\b\s(\w+\s){{{k}}}\bИТМО\b)'
        for m in re.finditer(pat, text):
            fragments.append(m.group(0))
    return fragments

class TestCheckText(unittest.TestCase):

    def assertSortedListEqual(self, list1, list2, msg=None):
        self.assertListEqual(sorted(list1), sorted(list2), msg)

    def test_case_1(self):
        text = "ВТ а б ИТМО ВТ ИТМО"
        expected = ["ВТ а б ИТМО", "ВТ ИТМО", "ВТ а б ИТМО ВТ ИТМО"]
        self.assertSortedListEqual(checkText(text), expected)
    
    def test_case_2(self):
        text = "ВТ ВТ ВТ ИТМО"
        expected = ["ВТ ВТ ВТ ИТМО", "ВТ ВТ ИТМО", "ВТ ИТМО"]
        self.assertSortedListEqual(checkText(text), expected)

    def test_case_3(self):
        text = "Кафедра ВТ появилась в университете ИТМО в 1937"
        expected = ["ВТ появилась в университете ИТМО"]
        self.assertSortedListEqual(checkText(text), expected)
    
    def test_case_4(self):
        text = "ВТ ИТМО ВТ ИТМО, я не зна какие ещё тесты придумать с ВТ и ИТМО"
        expected = ["ВТ ИТМО", "ВТ ИТМО", "ВТ ИТМО ВТ ИТМО", "ВТ и ИТМО"]
        self.assertSortedListEqual(checkText(text), expected)
    
    def test_case_5(self):
        text = "Если этот тест не проходит я пишу псж. Ухажу с ВТ и из ИТМО"
        expected = ["ВТ и из ИТМО"]
        self.assertSortedListEqual(checkText(text), expected)

loader = unittest.TestLoader()
suite = loader.loadTestsFromTestCase(TestCheckText)
runner = unittest.TextTestRunner()
runner.run(suite)



text = input("Введите текст: ")
fragments = checkText(text)
print(fragments)

