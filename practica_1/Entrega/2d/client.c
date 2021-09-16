#include <math.h>
#include <netdb.h>
#include <netinet/in.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <sys/time.h>
#include <sys/types.h>
#include <unistd.h>

// Para calcular tiempo
double dwalltime() {
  double sec;
  struct timeval tv;

  gettimeofday(&tv, NULL);
  sec = tv.tv_sec + tv.tv_usec / 1000000.0;
  return sec;
}

void error(char *msg) {
  perror(msg);
  exit(0);
}

int main(int argc, char *argv[]) {
  int sockfd, portno;
  struct sockaddr_in serv_addr;
  struct hostent *server;

  if (argc < 3) {
    fprintf(stderr, "Client:: usage %s hostname port\n", argv[0]);
    exit(0);
  }

  // Toma el numero de puerto de los argumentos
  portno = atoi(argv[2]);

  // Crea el file descriptor del socket para la conexion
  // 	AF_INET - familia del protocolo - ipv4 protocols internet
  // 	SOCK_STREAM - tipo de socket
  sockfd = socket(AF_INET, SOCK_STREAM, 0);

  if (sockfd < 0) {
    error("Client:: ERROR opening socket");
  }

  // Toma la direccion del server de los argumentos
  server = gethostbyname(argv[1]);
  if (server == NULL) {
    fprintf(stderr, "Client:: ERROR, no such host\n");
    exit(0);
  }
  bzero((char *)&serv_addr, sizeof(serv_addr));
  serv_addr.sin_family = AF_INET;

  // Copia la direccion ip y el puerto del servidor a la estructura del socket
  bcopy((char *)server->h_addr, (char *)&serv_addr.sin_addr.s_addr,
        server->h_length);
  serv_addr.sin_port = htons(portno);

  // Descriptor - direccion - tamaño direccion
  if (connect(sockfd, &serv_addr, sizeof(serv_addr)) < 0) {
    error("Client:: ERROR connecting");
  }

  // Send 4 messages, each with different sizes
  char buffer[lround(pow(10, 6))];
  int msg_len;
  double timestamp;

  for (int i = 3; i < 7; i++) {
    printf("10 a la %d\n", i);
    for (int j = 0; j < 1000; j++) {
      int msg_len = lround(pow(10, i));

      // Clean buffer
      bzero(buffer, msg_len);

      // Fill buffer with '$'
      memset(buffer, '$', msg_len);

      timestamp = dwalltime();

      // Send message to socket
      if (write(sockfd, buffer, msg_len) < 0) {
        error("Client:: ERROR writing to socket");
      }

      // espera confirmación del servidor
      read(sockfd, buffer, 8);

      printf("%f,", (dwalltime() - timestamp) / 2);
    }
    printf("\n\n\n");
  }
  close(sockfd);
}
