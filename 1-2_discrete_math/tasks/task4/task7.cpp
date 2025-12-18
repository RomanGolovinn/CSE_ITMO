#include <bits/stdc++.h>

//Писал эту штуку почти 1,5 часа. Можно балл за старание?

using namespace std;

const long long MOD = 1e9 + 7;

long long factorial(int n){
    long long f = 1;
    for (int i = 1; i <= n; ++i) f = (f*i) % MOD;
    return f;
}


long long modpow(long long a, long long b, long long MOD){
    long long res = 1;
    a %= MOD;
    while(b){
        if(b & 1) res = (res * a) % MOD;
        a = (a * a) % MOD;
        b >>= 1;
    }
    return res;
}

long long modinv(long long a, long long MOD){
    return modpow(a, MOD - 2, MOD);
}

long long count_c(int m, vector<int>& k){
    long long c = factorial(m);
    for(int ki : k){
        c = c * modinv(factorial(ki), MOD) % MOD;
    }
    return c;
}


bool check_k(vector<int> a, vector<int> k, int n, long long T, int m, int k_shot, int X){
    //трушный если k подходит
    long long S = 0;
    for (int i = 0; i < n; i++){
        S += k[i]*a[i];
    }
    S += m/k_shot * X;
    if (S < T) return false;

    int m_mod_k = int(m % k_shot == 0);

    for (int i = 0; i < n; i++){
        if (k[i] != 0){
            if ((S - a[i] - m_mod_k*X) >= T) return false;
        }
    }
    return true;
}

void recursive_trying(int i, int rest, int n, vector<int>& k, vector<int>& a,
    long long T, int m, int k_shot, int X, long long& sum_c){
    // Эта функция чем то похожа на dfs
    if (i == n-1){
        k[i] = rest;
        if (check_k(a, k, n, T, m, k_shot, X)){
           sum_c += count_c(m, k); 
        }
        return;
    }

    for (int x = 0; x <= rest; x++){
        k[i] = x;
        recursive_trying(i + 1, rest - x, n, k, a, T, m, k_shot, X, sum_c);
    }
}


void trying_k(int n, long long m_max, int k_shot, int X, long long T,
     vector<int> a, long long& sum_c){
    vector<vector<int>> all_k;
    for (int m = 1; m <= m_max; m++){
        vector<int> k(n);
        recursive_trying(0, m, n, k, a, T, m, k_shot, X, sum_c);
    }
}

long long slove(long long T, int n, vector<int> a, int k_shot, int X){
    long long m_max = T/(*min_element(a.begin(), a.end()));
    long long sum_c = 0;
    trying_k(n, m_max, k_shot, X, T, a, sum_c);
    return sum_c;
}

int main(){
    long long P, R;
    cin >> P >>R;
    long long T = max(P, R);

    int n;
    cin >> n;

    vector<int> a(n);
    int ai;
    for (int i = 0; i < n; i++){
        cin >> ai;
        a[i] = ai;
    }

    int k, X;
    cin >> k >> X;

    long long days = slove(T, n, a, k, X);
    cout << days;
    return 0;
}