import jade.core.*;
import java.io.*;
import java.util.*;

public class Agent3a extends Agent {

  private String destination;
  private String filenameDestination;
  private String filenameOrigin;
  private String filename;
  private String origin;
  private boolean isOpComplete = false;
  private byte[] ftpFile;
  private int offset = 0;
  private String op;

  protected void setup() {
    if (this.getArguments().length != 3) {
      System.err.println("Usage: Agent3 option <container> <filename>");
      System.err.println(" Options:");
      System.err.println("   -r: read from <filename> in <container>");
      System.err.println("   -w: write to <filename> in <container>");
      System.exit(1);
    }

    this.origin = this.here().getName();
    this.destination = this.getArguments()[1].toString();

    this.filename = this.getArguments()[2].toString();
    this.filenameOrigin = "/pdytr/3/filesOrigin/" + this.filename;
    this.filenameDestination = "/pdytr/3/filesDestination/" + this.filename;

    this.op = this.getArguments()[0].toString();

    if (this.op.equals("-w")) {
      this.ftpFile = Ftp.read(this.filenameOrigin, this.offset, 10000000);
      if (this.ftpFile != null)
        this.offset += this.ftpFile.length;
    }

    try {
      // Create a new container
      System.out.println("Agent3: Trying to move to " + this.destination);
      this.doMove(new ContainerID(this.destination, null));

    } catch (Exception e) {
      System.err.println("Agent3: Error while trying to move to " +
                         this.destination);
      e.printStackTrace();
    }
  }

  protected void afterMove() {
    System.out.println("Agent3: I have moved to " + this.here());

    if (this.op.equals("-r")) {
      this.readFile();
    } else if (this.op.equals("-w")) {
      this.writeFile();
    } else {
      System.err.println("Agent3: Unknown option " + this.op);
      System.exit(1);
    }
  }

  private void writeFile() {
    if (this.here().getName().equals(this.origin)) {

      this.ftpFile = Ftp.read(this.filenameOrigin, this.offset, 10000000);

      // termine de leer? entonces cambio sec op complete a true
      if (this.ftpFile == null) {
        this.doDelete();
      } else {
        this.offset += this.ftpFile.length;
      }

      // voy al destino
      this.doMove(new ContainerID(this.destination, null));

    } else {
      // estoy en destino
      Ftp.write(this.filenameDestination, this.ftpFile.length, this.ftpFile);
      this.doMove(new ContainerID(this.origin, null));
    }
  }

  private void readFile() {
    if (this.here().getName().equals(this.origin)) {
      // si estoy en el origen

      // escribo el lo leido en el destino
      Ftp.write(this.filenameOrigin, this.ftpFile.length, this.ftpFile);

      // voy al destino
      this.doMove(new ContainerID(this.destination, null));

    } else {
      // estoy en el destino
      // estoy copiando el archivo al origen

      // lee el archivo origianl del container destino
      this.ftpFile = Ftp.read(this.filenameDestination, this.offset, 10000000);

      // termine de leer? entonces cambio first op complete a true
      if (this.ftpFile == null) {
        this.doDelete();
      } else {
        this.offset += this.ftpFile.length;
      }

      // voy al origen
      this.doMove(new ContainerID(this.origin, null));
    }
  }
}
