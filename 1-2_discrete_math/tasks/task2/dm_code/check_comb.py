import sys 
import argparse

def get_args():
    args = argparse.ArgumentParser()
    args.add_argument("result_path", type=str)
    args.add_argument("output_path", type=str)
    return args


def load_test(ids):
    with open(f"tests_mn/test_{str(ids)}.in", 'r') as f:
        q = f.readlines()
    n,k = [int(x) for x in q[0][:-1].split()]
    return n,k

def load_result(ids, path, k):
    with open(f"{path}/result_{str(ids)}.out", 'r') as f:
        q = f.readlines()
    pos = [[int(x) for x in q[i][:-1].split()] for i in range(k)]
    return pos

def calc(test, result):
    cnt = 0
    n,k = test
    for i in range(k):
        for j in range(i+1, k):
            if (result[i][0] == result[j][0] or result[i][1] == result[j][1] or abs(result[i][0]-result[i][1])==abs(result[j][0]-result[j][1]) or result[i][0]+result[i][1] == result[j][0] + result[j][1]):
                cnt+=1
    return 4*k / (cnt*n+1)
    

def main():
    args = get_args()

    path = args.result_path
    save_path = args.output_path
    scores = []
    for i in range(1, 21):
        test = load_test(i)
        result = load_result(i, path, test[1])
        scores.append(calc(test, result))
    with open(save_path, 'w') as f:
        f.write(sum(scores)/len(scores))

if __name__ == '__main__':
    main()