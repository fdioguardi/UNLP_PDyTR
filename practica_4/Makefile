dirs := 1 2 3
op := -w
ftpFilename := archivo.pdf

all: clean build

help:
	@echo "—————————————————————————————————————————————"
	@echo "Usage: make [options]"
	@echo "Options:"
	@echo "  help:     Muestra esta ayuda"
	@echo "  all:      Borra y compila todo"
	@echo "  clean:    Borra todo"
	@echo "  build:    Compila todo"
	@echo "  server:   Ejecuta el 'server'"
	@echo "  rec:      Compila y ejecuta el ejercicio 1"
	@echo "  sum:      Compila y ejecuta el ejercicio 2"
	@echo "  ftp:      Compila y ejecuta el ejercicio 3a"
	@echo "  ftpb:     Compila y ejecuta el ejercicio 3b"
	@echo "—————————————————————————————————————————————"

build: buildrec buildsum buildftpa buildftpb

buildrec:
	cd 1 && rm -rf classes && mkdir classes && javac -cp ../lib/jade.jar:src -d classes src/Agent1.java

buildsum:
	cd 2 && rm -rf classes && mkdir classes && javac -cp ../lib/jade.jar:src -d classes src/Agent2.java

buildftpa:
	cd 3 && rm -rf classes && mkdir classes && javac -cp ../lib/jade.jar:src -d classes src/Agent3a.java

buildftpb:
	cd 3 && rm -rf classes && mkdir classes && javac -cp ../lib/jade.jar:src -d classes src/Agent3.java

server:
	java -cp lib/jade.jar:classes jade.Boot -gui -local-host 127.0.0.1

rec: buildrec
	cd 1 && java -cp ../lib/jade.jar:classes jade.Boot -gui -container -host localhost -agents mov:Agent1

sum: buildsum
	cd 2 && java -cp ../lib/jade.jar:classes jade.Boot -gui -container -host localhost -agents a2:Agent2'(Main-Container, /pdytr/2/files/numbers.csv)'

ftpa: buildftpa
	cd 3 && java -cp ../lib/jade.jar:classes jade.Boot -gui -container -host localhost -agents a3:Agent3a'($(op), Main-Container, $(ftpFilename))'

ftpb: buildftpb
	cd 3 && java -cp ../lib/jade.jar:classes jade.Boot -gui -container -host localhost -agents a3:Agent3'(Main-Container, $(ftpFilename))'

clean:
	rm -f APDescription.txt MTPs-Main-Container.txt || sudo rm -f APDescription.txt MTPs-Main-Container.txt
	rm -f 3/filesDestination/copy* 3/filesOrigin/* || rm -f 3/filesDestination/copy* 3/filesOrigin/*
	$(foreach dir, $(dirs), rm -rf $(dir)/classes || sudo rm -rf $(dir)/classes;)
