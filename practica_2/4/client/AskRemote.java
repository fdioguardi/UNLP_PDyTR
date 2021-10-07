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

			File file = new File(args[1]);
			FileInputStream stream = new FileInputStream(file);

			byte[] result = new byte[2];
			int offset = 0;
			int writing_amount;
			do {
				// read local file in buffer
				stream.read(result);

				// set amount of bytes to write in the server
				writing_amount = (file.length() - offset < result.length) ? (int)file.length() - offset : result.length;

				// ask the server to write
				offset += remote.escribir("result.txt", writing_amount, result);

			} while (offset < file.length());

			stream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
