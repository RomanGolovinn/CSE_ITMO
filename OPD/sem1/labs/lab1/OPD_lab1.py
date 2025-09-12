import os

dirs = ["test",[
                ["test1", 'file']
            ],
        "fff",[
            "f", "dir"
        ]
]

content = {}

rights = {}

def main(path, dirs):
    print("sss")
    for i in range(0, len(dirs)-2, 2):
        dir = dirs[i: i+2]
        print(dir)
        if type(dir[1]) != list:
            if type(dir[1]) == "file":
                with open(path + dir[0]) as file:
                    file.write(content[dir[0]])
                    os.chmod(path+dirs[0], rights[dir[0]])
            else:
                os.mkdir(path+dir[0])
                os.chmod[path+dir[0], rights[dir[0]]]
        else:
            os.mkdir(path + dir[0])
            main(path+dir[0], dir[1])


main(os.getcwd() + "\\", dirs)

