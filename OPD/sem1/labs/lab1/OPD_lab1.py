import os

dirs = ["lab0", 
    [
        ["burmy4", "f"],
        ["delcatty8", 
            [
                ["octillery", "f"],
                ["vileplume", "f"],
                ["ledian", "d"],
                ["dratini", "d"],
                ["gurdurr", "d"],
                ["unfezant", "f"]
            ]
        ],
        ["dragonair4", "f"],
        ["gengar9", "f"],
        ["gigalith6",
            [
                ["cottonee", "f"],
                ["lampent", "d"],
                ["venonat", "f"],
                ["blaziken", "f"],
                ["skiploom", "f"]
            ]
        ],
        ["gliscor9", 
            [
                ["arcanine", "f"],
                ["baltoy", "f"],
                ["poochyena", "f"],
                ["hippopotas", "f"]
            ]
        ]
    ]
]

content = {
    "burmy4": """Ходы  Bug Bite Electroweb Snore String Shot""",

    "octillery": """Ходы
Aurora Beam Bind Bounce Brine Defense Curl Double-Edge Dive Gunk Shot‡
Icy Wind Mud-Slap Rock Blast‡ Seismic Toss Seed Bomb Signal Beam Sleep
Talk Snore String Shot Swift Water Pulse""",

    "vileplume": """Развитие
способности  Effect Spore""",

    "unfezant": """satk=7 sdef=6
spd=9""",

    "dragonair4": """Развитие способности  Marvel Scale""",

    "gengar9": """Тип
диеты  Nullivore""",

    "cottonee": """Ходы  Covet Endeavor Giga Drain Helping
Hand Knock Off Seed Bomb Sleep Talk Snore Tailwind Worry Seed""",

    "venonat": """Способности  Tackle Disable Foresight Supersonic
Confusion Poisonpowder Leech Life Stun Spore Psybeam Sleep Powder
Signal Beam Zen Headbutt Poison Fang Psychic""",

    "skiploom": """satk=5 sdef=7
spd=5""",

    "arcanine": """Возможности  Overland=16 Surface=6 Jump=5 Power=4
Intelligence=4 Firestarter=0 Tracker=0""",

    "baltoy": """Тип диеты
    Terravore""",

    "poochyena": """Живёт Forest Grassland""",

    "hippopotas": """Возможности
    Overland=4 Surface=4 Underwater=2 Borow6=0 Jump=2 Power=2
    Intelligence=4"""
}

rights = {
    "burmy4": 0o404,
    "delcatty8": 0o662,
    "octillery": 0o640,
    "vileplume": 0o404,
    "ledian": 0o531,
    "dratini": 0o551,
    "gurdurr": 0o752,
    "unfezant": 0o660,
    "dragonair4": 0o004,
    "gengar9": 0o006,
    "gigalith6": 0o551,
    "cottonee": 0o604,
    "lampent": 0o674,
    "venonat": 0o620,
    "blaziken": 0o611,
    "skiploom": 0o444,
    "gliscor9": 0o657,
    "arcanine": 0o404,
    "baltoy": 0o624,
    "poochyena": 0o440,
    "hippopotas": 0o444,
}


def main(path, dirs):
    name = dirs[0]
    ipath = os.path.join(path, name)

    # если файл
    if isinstance(dirs[1], str):
        if not os.path.exists(ipath):
            if dirs[1] == "f":
                print(f"Создаю файл: {ipath}")
                with open(ipath, "w") as f:
                    if name in content:
                        f.write(content[name])
            elif dirs[1] == "d":
                print(f"Создаю папку: {ipath}")
                os.makedirs(ipath, exist_ok=True)

        if name in rights:
            os.chmod(ipath, rights[name])

    # если директория с содержимым
    else:
        print(f"Создаю папку: {ipath}")
        os.makedirs(ipath, exist_ok=True)
        for sub in dirs[1]:
            main(ipath, sub)


if __name__ == "__main__":
    main(os.getcwd(), dirs)


