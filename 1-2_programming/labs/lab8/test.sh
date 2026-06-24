#!/bin/bash

kubectl run load-generator --image=busybox:1.28 --restart=Never -- /bin/sh -c "for i in \$(seq 1 150); do (while true; do head -c 1000 /dev/urandom | nc lab8-server-service 1234; done) & done; wait"

sleep 3

cleanup() {
    kubectl delete pod load-generator
    exit 0
}

trap cleanup SIGINT

kubectl get hpa lab8-server-hpa -w