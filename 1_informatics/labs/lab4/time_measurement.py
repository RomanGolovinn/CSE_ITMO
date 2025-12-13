import time
from main import *
from from_json_to_toml import *

start = time.time()

for i in range(100):
    convert()

finish = time.time()

print("Время выполнения кода без библиотек 100 раз", finish-start)

start = time.time()

for i in range(100):
    convert_for_time_measurement()

finish = time.time()

print("Время выполнения кода с библиотеками 100 раз", finish-start)
