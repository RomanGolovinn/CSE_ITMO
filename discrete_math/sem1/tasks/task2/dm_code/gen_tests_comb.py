import random 

pairs = [[8, 16], [16, 32], [32, 128], [64, 512]]

def save(output_path, n,k,ids):
    with open(f"{output_path}/test_{str(ids)}.in", 'w') as f:
        f.write(f"{str(n)} {str(k)}\n")

ids = 0
for n, k in pairs:
    for j in range(5):
        cur_n = random.randint(n//2, n)
        cur_k = random.randint(cur_n, k)
        save("tests_comb", n,k,ids*5+j+1)
    ids+=1
