import re

def check_word(word : str, l1 : str, l2 : str, l3 : str, distance : int):
    excluded_chars = f'{l1}{l2}{l3}'
    pattern = rf'\b\S*{l1}[^{excluded_chars}]{{{distance}}}{l2}[^{excluded_chars}]{{{distance}}}{l3}\S*\b'
    if re.search(pattern, word):
        return True
    return False

def check_list_of_words(words, l1 : str, l2 : str, l3 : str, distance : int):
    right_words = []
    for s in words:
        if check_word(s, l1, l2, l3, distance):
            right_words.append(s)
    return right_words

tests = [
    check_list_of_words(["корма", "корка", "корчма", "краб"], "к", "р", "а", 1),
    check_list_of_words(["салон", "салат", "солонка", "солома", "сон", "слон"], "с", "л", "н", 1),
    check_list_of_words(["a" + "b"*1000 + "c" + "d"*1000 + "e"], "a", "c", "e", 1000),
    check_list_of_words("Необходимо выбрать три любых буквы и расстояние между ними."
    " С помощьюрегулярного выражения нужно найти все слова (последовательность символов ограниченная пробелами), в которых встречаются эти буквы"
    " в заданнойпоследовательности и расстояние (например, через один друг от друга)".split(), "д", "р", "у", 0),
    check_list_of_words(["abccd", "aabcd"], "a", "b", "c", 0)
]

results = [
    ["корма"],
    ["салон", "солонка"],
    ["a" + "b"*1000 + "c" + "d"*1000 + "e"],
    ["друг", "друга)"],
    ["abccd", "aabcd"]
]

def check_tests(tests, results):
    for i in range(5):
        if tests[i] == results[i]:
            print(f"test{i+1} done")
        else:
            print(f"test{i+1} felt")

check_tests(tests, results)

n = int(input("Введите количество слов: "))
l = input("Введите буквы через пробе: ").split()
dis = int(input("Введите расстояние между буквами: "))
words = []
for i in range(n):
    words.append(input("Введите слово: "))

print(check_list_of_words(words, l[0], l[1], l[2], dis))
