/*
* AskRemote.java
* a) Looks up for the remote object
* b) "Makes" the RMI
*/

import java.rmi.Naming; /* lookup */
import java.rmi.registry.Registry; /* REGISTRY_PORT */

import java.io.File;
import java.io.FileOutputStream;

public class AskRemote
{
	public static void main(String[] args)
	{
		/* Look for hostname and msg length in the command line */
		if (args.length != 1)
		{
			System.out.println("1 argument needed: (remote) hostname");
			System.exit(1);
		}

		try {
			String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
			IfaceRemoteClass remote = (IfaceRemoteClass) Naming.lookup(rname);

			// Reads from server. File is 171 bytes long.
			RemoteFile result = remote.leer("not_a_rickroll.txt", 0, 200);

			// Copy file locally, if file exists, overwrite it.
			File file = new File("local_files" + File.separator + "my_copy_of_something_that_is_definitely_not_a_rickroll.txt");

			if (file.exists() && file.isFile()) file.delete();

			FileOutputStream stream = new FileOutputStream(file);

			stream.write(result.getContent(), 0, result.getSize());
			stream.close();

			// Send copy to get copied.
			remote.escribir("okey_im_starting_to_doubt.txt", result.getSize(), result.getContent());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
