import re

pattern = r'(?=(\bВТ\b(?:\s+\w+){0,4}\s+\bИТМО\b))'

def check_text(text : str) -> []:
    fragments = []
    f1 = re.findall(pattern, text)
    if f1:
        for i in f1:
            fragments.append(i)
    return fragments

tests = [
    check_text("BТ а б ИТМО ВТ ИТМО"),
    check_text("ВТ ВТ ВТ ИТМО"),
    check_text("Кафедра ВТ появилась в университете ИТМО в 1937"),
    check_text("ВТ ИТМО ВТ ИТМО, я не зна какие ещё тесты придумать с ВТ и ИТМО"),
    check_text("Если этот тест не проходит я пишу псж. Ухажу с ВТ и из ИТМО") #Прошёл :)
]

results = [
    ["ВТ а б ИТМО", "ВТ ИТМО"],
    ["ВТ ВТ ВТ ИТМО", "ВТ ВТ ИТМО", "ВТ ИТМО"],
    ["ВТ появилась в университете ИТМО"],
    ["ВТ ИТМО", "ВТ ИТМО ВТ ИТМО", "ВТ ИТМО", "ВТ и ИТМО"],
    ["ВТ и из ИТМО"]
]

def check_results(tests, results):
    for i in range(5):
        print(tests[i], results[i])
        isOK = True
        if len(results[i]) != len(tests[i]):
            isOK = False
        for j in range(len(tests[i])):
            if tests[i].count(tests[i][j]) != results[i].count(tests[i][j]):
                isOK = False
        if isOK:
            print(f"test{i+1} done")
        else:
            print(f"test{i+1} felt")


check_results(tests, results)

text = input("Введите текст: ")
fragments = check_text(text)
print(fragments)

