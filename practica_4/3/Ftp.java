import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.Serializable;

public class Ftp {

  public static byte[] read(String name, int offset, int reading_amount) {
    try {
      File file = new File(name);

      if (!file.exists() || !file.isFile() || offset >= file.length()) {
        return null;
      }

      FileInputStream stream = new FileInputStream(file);

      byte[] result = new byte[((reading_amount > 0) &&
                                (file.length() - offset >= reading_amount))
                                   ? reading_amount
                                   : (int)(file.length() - offset)];

      stream.read(result, offset, result.length);
      stream.close();

      return result;

    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
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
