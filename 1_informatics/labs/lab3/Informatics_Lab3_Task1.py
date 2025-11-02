import re

def check_text(text : str) -> []:
    fragments = []
    for k in range(0, 5):
        pat = rf'(?=(\bВТ\b(?:\s+\S+){{{k}}}\s+\bИТМО\b))'
        for m in re.finditer(pat, text):
            fragments.append(m.group(1))
    return fragments

tests = [
    check_text("ВТ а б ИТМО ВТ ИТМО"),
    check_text("ВТ ВТ ВТ ИТМО"),
    check_text("Кафедра ВТ появилась в университете ИТМО в 1937"),
    check_text("ВТ ИТМО ВТ ИТМО, я не зна какие ещё тесты придумать с ВТ и ИТМО"),
    check_text("Если этот тест не проходит я пишу псж. Ухажу с ВТ и из ИТМО") #Прошёл :)
]

results = [
    ["ВТ а б ИТМО", "ВТ ИТМО", "ВТ а б ИТМО ВТ ИТМО"],
    ["ВТ ВТ ВТ ИТМО", "ВТ ВТ ИТМО", "ВТ ИТМО"],
    ["ВТ появилась в университете ИТМО"],
    ["ВТ ИТМО", "ВТ ИТМО ВТ ИТМО", "ВТ ИТМО", "ВТ и ИТМО"],
    ["ВТ и из ИТМО"]
]

def check_results(tests, results):
    for i in range(5):
        isOK = True
        if len(results[i]) != len(tests[i]):
            isOK = False
        for j in range(len(tests[i])):
            if tests[i].count(tests[i][j]) != results[i].count(tests[i][j]):
                isOK = False
        for j in range(len(results[i])):
            if tests[i].count(results[i][j]) != results[i].count(results[i][j]):
                isOK = False
        if isOK:
            print(f"test{i+1} done")
        else:
            print(f"test{i+1} felt")


check_results(tests, results)

text = input("Введите текст: ")
fragments = check_text(text)
print(fragments)

