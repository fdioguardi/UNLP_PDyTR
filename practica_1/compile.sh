#!/bin/sh

# Shell script to compile server.c and client.c

gcc -w server.c -o server.out -lm
gcc -w client.c -o server.out -lm
