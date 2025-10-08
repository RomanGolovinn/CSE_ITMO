import random 

def dst(a, b):
    return sum([(q-w)**2 for q,w in zip(a,b)])**0.5 

def save(output_path, ids, n, e, costs, weights):
    with open(f"{output_path}/test_{str(ids)}.in", 'w') as f:
        f.write(f"{str(n)} {str(len(e))}\n")
        f.write(f"{' '.join([str(x) for x in costs])}\n")
        f.write(f"{' '.join([str(x) for x in weights])}\n")
        for u, v in e:
            f.write(f"{str(u)} {str(v)}\n") 

def gen_random_graph(n, p, output_path, ids):
    costs = [random.randint(1, int(1e9)) for _ in range(n)]
    weights = [random.randint(1, int(1e9)) for _ in range(n)]
    e = []
    for i in range(n):
        for j in range(i+1,n):
            if random.random() < p: 
                e.append([i,j])
    save(output_path, ids, n, e, costs, weights)

def gen_random_k_graph(n, p, k, output_path, ids):
    kk = [random.randint(0,k-1) for _ in range(n)]
    vv = [[] for _ in range(k)]
    for i in range(n):
        vv[kk[i]].append(i)
    costs = [random.randint(1, int(1e9)) for _ in range(n)]
    weights = [random.randint(1, int(1e9)) for _ in range(n)]
    e = []
    for i1 in range(k):
        for j1 in range(i1+1, k):
            for i in vv[i1]:
                for j in vv[j1]:
                    if random.random() < p: 
                        e.append([i,j])
    save(output_path, ids, n, e, costs, weights)

def gen_random_dist_graph(n, p, v, output_path, ids):
    vv = [[random.random() for _ in range(v)] for _ in range(n)]
    costs = [random.randint(1, int(1e9)) for _ in range(n)]
    weights = [random.randint(1, int(1e9)) for _ in range(n)]
    e = []
    for i in range(n):
        for j in range(i+1,n):
            if dst(vv[i], vv[j]) < p: 
                e.append([i,j])
    save(output_path, ids, n, e, costs, weights)

def gen_random_k_dist_graph(n, p, v, k, output_path, ids):
    kk = [random.randint(0,k-1) for _ in range(n)]
    vv = [[random.random() for _ in range(v)] for _ in range(n)]
    vv2 = [[] for _ in range(k)]
    for i in range(n):
        vv2[kk[i]].append(i)
    costs = [random.randint(1, int(1e9)) for _ in range(n)]
    weights = [random.randint(1, int(1e9)) for _ in range(n)]
    e = []
    for i1 in range(k):
        for j1 in range(i1+1, k):
            for i in vv2[i1]:
                for j in vv2[j1]:
                    if dst(vv[i], vv[j]) < p: 
                        e.append([i,j])
    save(output_path, ids, n, e, costs, weights)


def gen_tests_n(n, bias=0, output_path="tests_mn"):
    for i, p in enumerate([0.5, 0.7, 0.9]):
        gen_random_graph(n, p=p, output_path=output_path, ids=4*i+1+bias)
        gen_random_k_graph(n, p=p, k=random.randint(2, n//2), output_path=output_path, ids=4*i+2+bias)
        gen_random_dist_graph(n, p=p*(3**(1/3)), v=random.randint(3, 10), output_path=output_path, ids=4*i+3+bias)
        gen_random_k_dist_graph(n, p=p*(3**(1/3)), v=random.randint(3, 10), k=random.randint(2, n//2), output_path=output_path, ids=4*i+4+bias)

nn = [5, 10, 50, 100, 200]
for i in range(1, len(nn)):
    gen_tests_n(random.randint(nn[i-1], nn[i]), (i-1)*12)