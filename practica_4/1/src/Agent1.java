import com.sun.management.OperatingSystemMXBean;
import jade.core.*;
import jade.wrapper.*;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Agent1 extends Agent implements Serializable {

  public class ContainerData implements Serializable {
    private String name;
    private boolean loaded = false;
    private double cpuLoad;
    private long freeMemory;

    public ContainerData(String name) { this.name = name; }

    public String getName() { return this.name; }

    public void load() { this.loaded = true; }

    public void setData(double cpuLoad, long freeMemory) {
      this.cpuLoad = cpuLoad;
      this.freeMemory = freeMemory;
    }

    public boolean isLoaded() { return this.loaded; }

    public long getFreeMemory() { return this.freeMemory; }

    public void print() {

      // print cpu load
      System.out.println("Nombre: " + this.name +
                         ", CPU load: " + this.cpuLoad +
                         ", Memoria disponible: " + this.freeMemory);
    }
  }

  private String origin;
  private List<ContainerData> containers = new ArrayList<>();
  private long startTime;

  protected void setup() {

    // Add containers to visit
    String[] names = new String[] {"Container-i", "Container-ii",
                                   "Container-iii", "Container-iv"};
    for (String container : names) {
      this.containers.add(new ContainerData(container));
    }

    this.origin = here().getName();

    /* Create containers */

    // Get the JADE runtime interface (singleton)
    jade.core.Runtime runtime = jade.core.Runtime.instance();

    for (ContainerData containerData : this.containers) {

      // Create a Profile, where the launch arguments are stored
      Profile profile = new ProfileImpl();
      profile.setParameter(Profile.CONTAINER_NAME, containerData.getName());
      profile.setParameter(Profile.MAIN_HOST, "localhost");

      // create a non-main agent container
      ContainerController container = runtime.createAgentContainer(profile);
    }

    // start counting, we don't care about the origin's information
    this.startTime = System.currentTimeMillis();

    // go to next container
    this.doMove(this.nextContainer());
  }

  private ContainerID nextContainer() {
    for (ContainerData cd : this.containers) {
      if (!cd.isLoaded()) {
        try {
          cd.load();
          return new ContainerID(cd.getName(), null);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    return new ContainerID(this.origin, null);
  }

  private void printInfo() {

    // print total time
    System.out.println("Tiempo total (ms) del recorrido: " +
                       (System.currentTimeMillis() - this.startTime));

    long memsum = 0;
    for (ContainerData cd : this.containers) {
      cd.print();
      memsum += cd.getFreeMemory();
    }
    System.out.println("Total de memoria libre: " + memsum);

    System.out.println(
        "\n\n--------------------------------------------------------\n\n");
  }

  private void reset() {
    List<ContainerData> newData = new ArrayList<>();
    for (ContainerData cd : this.containers) {
      newData.add(new ContainerData(cd.getName()));
    }
    this.containers = newData;
    this.startTime = System.currentTimeMillis();
  }

  protected void afterMove() {
    // if we are back at origin
    if (this.here().getName().equals(this.origin)) {
      this.printInfo();
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      this.reset();
      this.doMove(this.nextContainer());
    } else {
      // get data to load
      ContainerData current = null;
      for (ContainerData cd : this.containers) {
        if (cd.getName().equals(this.here().getName())) {
          current = cd;
          break;
        }
      }

      // create os bean
      OperatingSystemMXBean osBean =
          ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

      current.setData(osBean.getSystemCpuLoad(),
                      osBean.getFreePhysicalMemorySize());

      // move to next container
      this.doMove(this.nextContainer());
    }
  }
}
