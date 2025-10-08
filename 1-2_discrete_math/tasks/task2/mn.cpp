#include <bits/stdc++.h>
using namespace std;

int n, m;
vector<vector<int>> not_graph;
vector<vector<int>> all_cliques;

void bronKerbosch(vector<int> R, vector<int> P, vector<int> X) {
    if (P.empty() && X.empty()) {
        all_cliques.push_back(R);
        return;
    }

    int u = -1;
    if (!P.empty()) u = P[0];
    else if (!X.empty()) u = X[0];

    vector<int> P_without_neighbors;
    if (u != -1) {
        for (int v : P) {
            if (!not_graph[u][v]) {
                P_without_neighbors.push_back(v);
            }
        }
    } else {
        P_without_neighbors = P;
    }

    for (int v : P_without_neighbors) {
        vector<int> R_new = R;
        R_new.push_back(v);

        vector<int> P_new, X_new;
        for (int w : P) if (not_graph[v][w]) P_new.push_back(w);
        for (int w : X) if (not_graph[v][w]) X_new.push_back(w);

        bronKerbosch(R_new, P_new, X_new);

        P.erase(remove(P.begin(), P.end(), v), P.end());
        X.push_back(v);
    }
}

vector<vector<int>> build_not_g(const vector<vector<int>>& graph){
    vector<vector<int>> comp(n, vector<int>(n, 0));
    for (int i = 0; i < n; i++){
        for (int j = 0; j < n; j++){
            if (i != j) comp[i][j] = 1 - graph[i][j];
        }
    }
    return comp;
}

pair<double, vector<int>> find_best(const vector<vector<int>>& all_c,
                                   const vector<int>& costs,
                                   const vector<int>& weights) {
    double best_q = -1.0;
    vector<int> best_set;

    for (auto& clique : all_c) {
        int c = 0, w = 0;
        for (int v : clique) {
            c += costs[v];
            w += weights[v];
        }
        if (w == 0) continue; // защита от деления на ноль
        double q = (double)c / w;
        if (q > best_q) {
            best_q = q;
            best_set = clique;
        }
    }
    return {best_q, best_set};
}

int main(){
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> n >> m;
    vector<vector<int>> graph(n, vector<int>(n, 0));

    vector<int> costs(n);
    vector<int> weights(n);

    for (int i = 0; i < n; i++){
        cin >> costs[i];
    }
    for (int i = 0; i < n; i++){
        cin >> weights[i];
    }

    for (int i = 0; i < m; i++){
        int n1, n2;
        cin >> n1 >> n2;
        graph[n1][n2] = 1;
        graph[n2][n1] = 1;
    }

    // строим дополнение графа
    not_graph = build_not_g(graph);

    // запускаем Bron–Kerbosch
    vector<int> R, P, X;
    for (int i = 0; i < n; i++) P.push_back(i);
    bronKerbosch(R, P, X);

    // выбираем лучшее множество
    auto [best_q, best_set] = find_best(all_cliques, costs, weights);

    // вывод ответа
    cout << (int)best_set.size() << "\n";
    for (int v : best_set) cout << v << " ";
    cout << "\n";

    return 0;
}


