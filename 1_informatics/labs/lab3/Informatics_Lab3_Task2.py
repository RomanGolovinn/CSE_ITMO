import re

def check_word(word : str, l1 : str, l2 : str, l3 : str, distance : int) -> []:
    pattern = rf'{l1}([а-яА-Я]){{{distance}}}{l2}([а-яА-Я]){{{distance}}}{l3}'
    if not re.fullmatch(pattern, word):
        return False
    if word.count(l1) == 1 and word.count(l2) == 1 and word.count(l3) == 1:
        return True
    return False

print(check_word("корма", "к", "р", "а", 1))