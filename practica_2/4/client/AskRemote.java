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
import java.io.FileInputStream;

import shared.IfaceRemoteClass;
import shared.IfaceRemoteFile;

class AskRemote {

	public static void main(String[] args) {
		/* Look for hostname and process id in the command line */
		if (args.length != 2)
		{
			System.out.println("2 arguments needed: (remote) hostname, filename");
			System.exit(1);
		}

		try {
			String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
			IfaceRemoteClass remote = (IfaceRemoteClass) Naming.lookup(rname);

			String filename = args[1];
			File file = new File("client" + File.separator + "files", filename);

			FileInputStream stream = new FileInputStream(file);

			int max_size = 2;
			byte[] result = new byte[max_size];
			int offset = 0;
			int send_amount;
			do {
				// read local file in buffer
				stream.read(result);

				// set amount of bytes to write in the server
				send_amount = (file.length() - offset < max_size) ? (int)file.length() - offset : max_size;

				// ask the server to write
				remote.escribir("result", send_amount, result);

				// increment offset to know when to stop iterating
				offset += send_amount;

			} while (offset < file.length());

			stream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
