dirs := 1 2

all: clean build

help:
	@echo "————————————————————————————————————————————"
	@echo "Usage: make [options]"
	@echo "Options:"
	@echo "  help:     Muestra esta ayuda"
	@echo "  all:      Borra y compila todo"
	@echo "  clean:    Borra todo"
	@echo "  build:    Compila todo"
	@echo "  server:   Ejecuta el 'server'"
	@echo "  rec:      Compila y ejecuta el ejercicio 1"
	@echo "  sum:      Compila y ejecuta el ejercicio 2"
	@echo "————————————————————————————————————————————"

build: buildrec buildsum

buildrec:
	@echo "build 1"
	#cd 1 && mkdir classes && javac -cp ../lib/jade.jar:src -d classes src/Agent1.java

buildsum:
	cd 2 && mkdir classes && javac -cp ../lib/jade.jar:src -d classes src/Agent2.java

server:
	java -cp lib/jade.jar:classes jade.Boot -gui -local-host 127.0.0.1

rec: buildrec
	cd 1 && java -cp ../lib/jade.jar:classes jade.Boot -gui -container -host localhost -agents mov:Agent1

sum: buildsum
	cd 2 && java -cp ../lib/jade.jar:classes jade.Boot -gui -container -host localhost -agents a2:Agent2'(Main-Container, /pdytr/2/files/numbers.csv)'

clean:
	- sudo rm -f APDescription.txt MTPs-Main-Container.txt
	- $(foreach dir, $(dirs), sudo rm -rf $(dir)/classes;)
