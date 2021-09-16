#!/bin/sh

# Corre el ejercicio 2b de la práctica 1 de PDyTR.

gcc -w client.c -lm -o client.out
gcc -w server.c -lm -o server.out

./server.out 3500 &
./client.out localhost 3500 &
wait
