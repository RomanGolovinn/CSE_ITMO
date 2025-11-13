import re
import unittest

def checkWord(word : str, l1 : str, l2 : str, l3 : str, distance : int):
    excluded_chars = f'{l1}{l2}{l3}'
    pattern = rf'\b\S*{l1}[^{excluded_chars}]{{{distance}}}{l2}[^{excluded_chars}]{{{distance}}}{l3}\S*\b'
    if re.search(pattern, word):
        return True
    return False

def checkListOfWords(words, l1 : str, l2 : str, l3 : str, distance : int):
    right_words = []
    for s in words:
        if checkWord(s, l1, l2, l3, distance):
            right_words.append(s)
    return right_words

class TestCheckText(unittest.TestCase):
    def assertSortedListEqual(self, list1, list2, msg=None):
        self.assertListEqual(sorted(list1), sorted(list2), msg)
    
    def testCase1(self):
        words = ["корма", "корка", "корчма", "краб"]
        l1 = "к"
        l2 = "р"
        l3 = "а"
        distance = 1
        expected = ["корма"]
        self.assertSortedListEqual(checkListOfWords(words, l1, l2, l3, distance), expected)
    
    def testCase2(self):
        words = ["салон", "салат", "солонка", "солома", "сон", "слон"]
        l1 = "с"
        l2 = "л"
        l3 = "н"
        distance = 1
        expected = ["салон", "солонка"]
        self.assertSortedListEqual(checkListOfWords(words, l1, l2, l3, distance), expected)
    
    def testCase3(self):
        words = ["a" + "b"*1000 + "c" + "d"*1000 + "e"]
        l1 = "a"
        l2 = "c"
        l3 = "e"
        distance = 1000
        expected = ["a" + "b"*1000 + "c" + "d"*1000 + "e"]
        self.assertSortedListEqual(checkListOfWords(words, l1, l2, l3, distance), expected)
    
    def testCase4(self):
        words = ("Необходимо выбрать три любых буквы и расстояние между ними." +
        " С помощьюрегулярного выражения нужно найти все слова (последовательность символов ограниченная пробелами), в которых встречаются эти буквы"+
        " в заданнойпоследовательности и расстояние (например, через один друг от друга)").split()
        l1 = "д"
        l2 = "р"
        l3 = "у"
        distance = 0
        expected = ["друг", "друга)"]
        self.assertSortedListEqual(checkListOfWords(words, l1, l2, l3, distance), expected)

    def testCase5(self):
        words = ["abccd", "aabcd"]
        l1 = "a"
        l2 = "b"
        l3 = "c"
        distance = 0
        expected = ["abccd", "aabcd"]
        self.assertSortedListEqual(checkListOfWords(words, l1, l2, l3, distance), expected)
    

loader = unittest.TestLoader()
suite = loader.loadTestsFromTestCase(TestCheckText)
runner = unittest.TextTestRunner()
runner.run(suite)

n = int(input("Введите количество слов: "))
l = input("Введите буквы через пробе: ").split()
dis = int(input("Введите расстояние между буквами: "))
words = []
for i in range(n):
    words.append(input("Введите слово: "))

print(checkListOfWords(words, l[0], l[1], l[2], dis))
