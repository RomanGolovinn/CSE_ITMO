#!/bin/bash
mkdir -p results
i=1
for file in tests_mn/*.in; do
    ./mn < "$file" > "results/result_${i}.out"
    ((i++))
done
