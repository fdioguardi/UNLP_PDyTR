import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;

public class Ftp {

  public static byte[] read(String name, int offset, int reading_amount) {
    File file = new File(name);

    if (!file.exists() || !file.isFile() || offset >= file.length()) {
      return null;
    }

    byte[] result = null;

    try {

      FileInputStream fi = new FileInputStream(file);

      fi.skip(offset);

      reading_amount = reading_amount > 0 ? reading_amount : 10000000;
      reading_amount = Math.min(reading_amount, (int)(file.length() - offset));

      result = new byte[reading_amount];

      fi.read(result, 0, result.length);

      System.out.println("Total bytes read: " + offset);
      fi.close();

    } catch (Exception e) {
      e.printStackTrace();

    } finally {
      return result;
    }
  }

  public static int write(String name, int writing_amount, byte[] data) {
    try {
      File file = new File(name);
      FileOutputStream stream = new FileOutputStream(file, true);

      stream.write(data, 0, writing_amount);
      stream.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return writing_amount;
  }

  public boolean exists(String name) {
    File file = new File(name);
    return file.exists() && file.isFile();
  }
}
