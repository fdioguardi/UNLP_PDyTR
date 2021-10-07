/*
* AskRemote.java
* a) Looks up for the remote object
* b) "Makes" the RMI
*/

package client;

import java.rmi.Naming; /* lookup */
import java.rmi.registry.Registry; /* REGISTRY_PORT */

import java.io.File;
import java.io.FileOutputStream;

import shared.IfaceRemoteClass;
import shared.IfaceRemoteFile;

class AskRemote {

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
			String filename = "file.txt";
			if (!remote.exists(filename)) {
				byte[] content = "Never gonna give you up Never gonna let you down Never gonna run around and desert you Never gonna make you cry Never gonna say goodbye Never gonna tell a lie and hurt you".getBytes();
				remote.escribir(filename, content.length, content);
			}

			// Read from server.
			IfaceRemoteFile result = remote.leer(filename, 0, -1);

			if (!result.isEmpty()) {

				// Copy file locally, if file exists, overwrite it.
				File file = new File("client" + File.separator + "files", filename);

				if (file.exists() && file.isFile()) {
					file.delete();
				}

				FileOutputStream stream = new FileOutputStream(file);

				stream.write(result.getContent(), 0, result.getSize());
				stream.close();

				// Send copy to get copied.
				remote.escribir("copy_" + filename, result.getSize(), result.getContent());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
