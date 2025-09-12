import os

dirs = ["lab0",
    ["test", ["f", "f"], ["d", "d"]],
    ["test2", "d"]
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

