import os

dirs = ["lab0", 
        [
        "larvitar4", 
            [
            "gothitelle", 
                [
                "mandibuzz", 
                    ["swoobat", "d"],
                    ["electrike", "d"],
                    ["eevee", "f"],
                    ["shuppet", "f"],
                    ["snivy", "d"],
                    ["cranidos", "d"],
                    ["manectric", "d"]
                ],
                [
                "tyranitar", 
                    ["starui", "d"],
                    ["poliwhirl", "f"]
                ],
                [
                "bibarel",
                    ["spoink", "d"],
                    ["bulbasaur", "f"],
                    ["infernapee", "d"],
                    ["vanillite", "d"],
                    ["zebstrika", "f"],
                    ["leavanny", "d"],
                    ["slowpoke", "d"]
                ],
                [
                "mantyke",
                    ["nidorina", "d"],
                    ["xaty", "f"],
                    ["slaking", "d"],
                    ["typhlosion", "d"],
                    ["vileplume", "f"],
                    ["vaporeon", "f"]
                ],
                [
                "dugtrio",
                    ["walrein", "d"],
                    ["gothitelle", "f"],#могут быть проблеммы с одноимённой пакой при назначени прав
                    ["dewgong", "d"],
                    ["happiny", "d"],
                    ["drilbur", "d"]
                ]
            ],
            [
            "wingull",
                [
                "crustle",
                    [""]        
                ]
            ]
        ],
    ]

content = {}

rights = {}

def main(path, dirs):
    name = dirs[0]
    ipath = path+name
    if type(dirs[1]) == str:
        if not os.path.exists(ipath):
            if dirs[1] == "f":
                with open(ipath, "w") as f:
                    if name in content:
                        f.write(content[name])
            if dirs[1] == "d":
                os.mkdir(ipath)
        if name in rights:
            os.chmod(ipath, rights[name])
    else:
        if not os.path.exists(ipath):
            os.mkdir(ipath)
        listofdir = dirs[1:]
        for i in listofdir:
            main(ipath + "\\", i)


main(os.getcwd() + "\\", dirs)

