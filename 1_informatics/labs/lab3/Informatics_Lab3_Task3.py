import re

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
    print(re.findall(pattern, text), core)
    return re.findall(pattern, text)[index]

def changeWord(text : str, index : int):
    smallCaseText = text.lower()
    core = findCore(smallCaseText)
    word = findWord(smallCaseText, core, index)
    text = re.sub(rf'{core}\S+', word, text)
    text = re.sub(rf'{core}\S+'[0].upper() + rf'{core}\S+'[1:], word[0].upper() + word[1:], text)
    return text

print(changeWord("Футбольный клуб «Реал Мадрид» является 15-кратным обладателем главного "
"футбольного европейского трофея – Лиги Чемпионов. Данный турнир организован "
"Союзом европейских футбольных ассоциаций (УЕФА). Идея о континентальном "
"футбольном турнире пришла к журналисту Габриэлю Ано в 1955 году.", 2-1))
    