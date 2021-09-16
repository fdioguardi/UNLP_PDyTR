/*
 * EchoServer.java
 * Just receives some data and sends back a "message" to a client
 *
 * Usage:
 * java Server port
 */

import java.io.*;
import java.net.*;

import java.lang.Math;

public class Server
{
  public static void main(String[] args) throws IOException
  {
    /* Check the number of command line parameters */
    if ((args.length != 1) || (Integer.valueOf(args[0]) <= 0) )
    {
      System.out.println("1 arguments needed: port");
      System.exit(1);
    }

    /* The server socket */
    ServerSocket serverSocket = null;
    try
    {
      serverSocket = new ServerSocket(Integer.valueOf(args[0]));
    }
    catch (Exception e)
    {
      System.out.println("Error on server socket");
      System.exit(1);
    }

    /* The socket to be created on the connection with the client */
    Socket connected_socket = null;

    try /* To wait for a connection with a client */
    {
      connected_socket = serverSocket.accept();
    }
    catch (IOException e)
    {
      System.err.println("Error on Accept");
      System.exit(1);
    }

    /* Streams from/to client */
    DataInputStream fromclient;
    DataOutputStream toclient;

    /* Get the I/O streams from the connected socket */
    fromclient = new DataInputStream(connected_socket.getInputStream());
    toclient   = new DataOutputStream(connected_socket.getOutputStream());

    /* Buffer to use with communications (and its length) */
    byte[] buffer = new byte[(int)Math.pow(10, 6)];

    /* Recv data from client */
    int bytes_read = fromclient.read(buffer);

    System.out.println("Server:: Length of message recieved: " + bytes_read + " bytes_read");
    System.out.println("---------------------------------------------------------------------");

    /* Close everything related to the client connection */
    fromclient.close();
    toclient.close();
    connected_socket.close();
    serverSocket.close();
  }
}
