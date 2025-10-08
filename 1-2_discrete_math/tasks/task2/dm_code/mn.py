import argparse

def get_args():
    parser = argparse.ArgumentParser()
    parser.add_argument("result_path", type=str, help="Путь к папке с результатами")
    parser.add_argument("output_path", type=str, help="Файл для записи итогового результата")
    return parser.parse_args()  # <- parse_args() обязательно!

def load_test(ids):
    with open(f"tests_mn/test_{ids}.in", 'r') as f:
        q = f.readlines()
    n, m = [int(x) for x in q[0].strip().split()]
    costs = [int(x) for x in q[1].strip().split()]
    weights = [int(x) for x in q[2].strip().split()]
    e = []
    for line in q[3:]:
        e.append([int(x) for x in line.strip().split()])
    return n, e, costs, weights

def load_result(ids, path):
    with open(f"{path}/result_{ids}.out", 'r') as f:
        q = f.readlines()
    v = [int(x) for x in q[1].strip().split()]
    return v

def calc(test, result):
    used = set()
    n, e, costs, weights = test
    g = [[] for _ in range(n)]
    for u, v in e:
        g[u].append(v)
        g[v].append(u)
    for i in range(n):
        used.add(i)
        for u in g[i]:
            used.add(u)
            if u in result:
                return max(weights) * n
    if len(used) != n:
        return max(weights) * n
    return sum([costs[j] for j in result]) / sum([weights[j] for j in result])

def main():
    args = get_args()

    path = args.result_path
    save_path = args.output_path
    scores = []
    for i in range(1, 49):
        test = load_test(i)
        result = load_result(i, path)
        scores.append(calc(test, result))
    with open(save_path, 'w') as f:
        f.write(str(sum(scores)/len(scores)))

if __name__ == '__main__':
    main()
