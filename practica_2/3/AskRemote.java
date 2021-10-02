/*
* AskRemote.java
* a) Looks up for the remote object
* b) "Makes" the RMI
*/

import java.rmi.Naming; /* lookup */
import java.rmi.registry.Registry; /* REGISTRY_PORT */

import java.io.File;
import java.io.FileOutputStream;

public class AskRemote {

	public static void main(String[] args) {
		/* Look for hostname and msg length in the command line */
		if (args.length != 1)
		{
			System.out.println("1 argument needed: (remote) hostname");
			System.exit(1);
		}

		try {
			String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
			IfaceRemoteClass remote = (IfaceRemoteClass) Naming.lookup(rname);

			// Ensure there's something on the server we can copy.
			File server_file = new File("files" + File.separator + "not_a_rickroll.txt");
			if (! server_file.exists()) {
				byte[] content = "Never gonna give you up Never gonna let you down Never gonna run around and desert you Never gonna make you cry Never gonna say goodbye Never gonna tell a lie and hurt you".getBytes();
				remote.escribir("not_a_rickroll.txt", content.length, content);
			}

			// Read from server. File is 171 bytes long.
			RemoteFile result = remote.leer("not_a_rickroll.txt", 0, 200);

			if (!result.isEmpty()) {

				// Copy file locally, if file exists, overwrite it.
				File file = new File("local_files" + File.separator + "my_copy_of_something_that_is_definitely_not_a_rickroll.txt");

				if (file.exists() && file.isFile()) file.delete();

				FileOutputStream stream = new FileOutputStream(file);

				stream.write(result.getContent(), 0, result.getSize());
				stream.close();

				// Send copy to get copied.
				remote.escribir("okey_im_starting_to_doubt.txt", result.getSize(), result.getContent());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
