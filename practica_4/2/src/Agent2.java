import jade.core.*;
import java.io.*;
import java.util.*;

public class Agent2 extends Agent {

  private String[] args = new String[2];
  private String origin;
  private double sum;

  protected void setup() {
    if (this.getArguments().length != 2) {
      System.err.println("Usage: Agent2 <file_location> <filename>");
      System.exit(1);
    }

    for (int i = 0; i < this.getArguments().length; i++) {
      args[i] = this.getArguments()[i].toString();
    }

    this.origin = here().getName();

    try {
      // Create a new container
      ContainerID destination = new ContainerID(this.args[0], null);
      System.out.println("Agent2: Trying to move to " + destination.getName());
      this.doMove(destination);

    } catch (Exception e) {
      System.out.println("Agent2: Error while trying to move to " +
                         this.args[0]);
      e.printStackTrace();
    }
  }

  protected void afterMove() {
    System.out.println("Agent2: I have moved to " + here());

    if (here().getName().equals(this.origin)) {
      System.out.println("Agent2: I am back to my origin");
      System.out.println("Agent2: Sum of numbers in " + this.args[1] + ": " +
                         this.sum);
      this.doDelete();
    } else {

      try {
        System.out.println("Agent2: Reading file " + this.args[1]);

        Scanner scanner = new Scanner(new File(this.args[1]));
        while (scanner.hasNextDouble()) {
          this.sum += scanner.nextDouble();
        }
        scanner.close();

      } catch (Exception e) {
        System.err.println("Agent2: Error while reading file " + this.args[1]);
        e.printStackTrace();
        System.exit(1);
      }

      this.doMove(new ContainerID(this.origin, null));
    }
  }
}
