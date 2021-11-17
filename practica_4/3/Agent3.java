import jade.core.*;
import java.io.*;
import java.util.*;

public class Agent3 extends Agent {

  private String[] args = new String[2];
  private byte[] ftpFile;
  private String origin;
  private String destination;
  private String filenameOrigin;
  private String filenameDestination;
  private boolean isFirstOpComplete = false;
  private boolean isSecOpComplete = false;

  protected void setup() {
    if (this.getArguments().length != 2) {
      System.err.println("Usage: Agent3 <container> <filename>");
      System.exit(1);
    }

    this.origin = this.here().getName();
    this.destination = this.getArguments()[0].toString();
    
    this.filenameOrigin = "/pdytr/3/filesOrigin/" + this.getArguments()[1].toString();
    this.filenameDestination = "pdytr/3/filesDestination/" + this.getArguments()[1].toString();


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

    if (this.here().getName().equals(this.origin)) {
      // si estoy en el origen

    	if(!this.isFirstOpComplete){

	    	// escribo el lo leido en el destino
	    	Ftp.write(this.filenameOrigin, this.ftpFile.length, this.ftpFile);

    	} else {
    		// copiar archivo local en destino

				// lee el archivo local
				this.ftpFile = Ftp.read(this.filenameOrigin, 0, -1);

				// termine de leer? entonces cambio sec op complete a true
				if(this.ftpFile == null){
					this.stop();
				}
    		}

    	// voy al destino
		this.doMove(new ContainerID(this.destination, null));

    } else {
    	// estoy en el destino
    	if(!this.isFirstOpComplete){
    		// estoy copiando el archivo al origen

			// lee el archivo origianl del container destino
			this.ftpFile = Ftp.read(this.filenameDestination, 0, -1);

			// termine de leer? entonces cambio first op complete a true
			if(this.ftpFile == null){
				this.isFirstOpComplete = true;
			}

    	} else {
    		// Escribo los datos leidos de origen
    		Ftp.write(this.filenameDestination, this.ftpFile.length, this.ftpFile);
    	}

		// voy al origen
		this.doMove(new ContainerID(this.origin, null));
    }

  }

  private void stop(){
  	this.onDelete();
  	System.exit(0);
  }

}
