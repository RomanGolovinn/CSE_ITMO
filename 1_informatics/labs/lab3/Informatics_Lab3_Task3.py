#Golovin Roman Evgenevich
#Group P3116
#Date 15.11.2025

import re
import unittest

pattern1 = r"([а-яёА-Я]+)(?:ий|ый|его|ого|ему|ому|уго|ий|ого|ый|им|ым|ем|ом|ая|яя|ей|ой|ую|юю|ее|ое|его|ого|ему|ому|им|ым|ем|ом)"
pattern2 = r"([а-яёА-Я]+)(?:н|ан|ян|ин|сл|ств|ов|ев|нн|енн|ат|ив|лив|чив|оньк|еньк|к|ск)\S+"

def findCore(text : str):
    words = text.split()
    cores = {}
    for i in words:
        if re.match(pattern1, i) and re.match(pattern2, i):
            core = [x.group(1) for x in re.finditer(pattern1, i)][0]
            if len(re.findall(rf'{core}', text)) > 1 and core != '':
                if core in cores:
                    cores[core] += 1
                else:
                    cores[core] = 1
    
    max_key = max(cores, key=cores.get)
    return max_key
    

def findWord(text : str, core : str, index : int):
    pattern = rf'{core}\S+'
    return re.findall(pattern, text)[index]

def changeWord(text : str, index : int):
    smallCaseText = text.lower()
    core = findCore(smallCaseText)
    word = findWord(smallCaseText, core, index)
    text = re.sub(rf'{core}\S+', word, text)
    text = re.sub(rf'{core}\S+'[0].upper() + rf'{core}\S+'[1:], word[0].upper() + word[1:], text)
    return text


class TestChangeWord(unittest.TestCase):
    def assertTextEqual(self, text1, text2, msg=None):
        self.assertEqual(text1, text2, msg)

    def testCase1(self):
        text = ("Река проложила длинный маршрут сквозь высокие горы. " +
        "Путешественник мечтал о конце этого длинного пути, полном испытаний. " +
        "Он решил отдохнуть под длинными деревьями.")

        excepted = ("Река проложила длинный маршрут сквозь высокие горы. "+
        "Путешественник мечтал о конце этого длинный пути, полном испытаний. "+
        "Он решил отдохнуть под длинный деревьями.")

        self.assertTextEqual(changeWord(text, 0), excepted)
    
    def testCase2(self):
        text = ("Художник написал красивую картину" +
                "Красивые фигуры были как живые. " +
                "Задний фон переливался красивыми цветами.")
        expected = ("Художник написал красивыми картину" +
                "Красивыми фигуры были как живые. " +
                "Задний фон переливался красивыми цветами.")
        
        self.assertTextEqual(changeWord(text, 2), expected)
    
    def testCase3(self):
        text = ("Зенит» является многократным обладателем важного футбольного трофея страны." +
                "Данный турнир организован Союзом футбольных ассоциаций России. " +
                "Идея о крупном футбольном чемпионате появилась у одного известного спортивного функционера в далёком 1990 году. ")
        expected = ("Зенит» является многократным обладателем важного футбольного трофея страны." +
                "Данный турнир организован Союзом футбольного ассоциаций России. " +
                "Идея о крупном футбольного чемпионате появилась у одного известного спортивного функционера в далёком 1990 году. ")
        
        self.assertTextEqual(changeWord(text, 0), expected)

    def testCase4(self):
        text = "Красный красным краски красного красивый"
        expected = "Красного красного краски красного красивый"

        self.assertTextEqual(changeWord(text, 2), expected)
    
    def testCase5(self):
        test = ("Капитан увидел вдали синий парус одинокого судна. "+
        "Утром облака рассеялись над поверхностью синего моря. "+
        "Мальчик поделился секретом с товарищем о маленьком синем камне, найденном на берегу.")
        expected = ("Капитан увидел вдали синего парус одинокого судна. "+
        "Утром облака рассеялись над поверхностью синего моря. "+
        "Мальчик поделился секретом с товарищем о маленьком синего камне, найденном на берегу.")

        self.assertTextEqual(changeWord(test, 1), expected)

loader = unittest.TestLoader()
suite = loader.loadTestsFromTestCase(TestChangeWord)
runner = unittest.TextTestRunner(verbosity=2)
runner.run(suite)

i = int(input("Введите номер повторяющегося слова: "))
text = input("Введите текст: ")
print(changeWord(text, i))