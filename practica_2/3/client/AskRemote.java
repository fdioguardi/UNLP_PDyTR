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
		/* Look for hostname and msg length in the command line */
		if (args.length != 3)
		{
			System.out.println("2 arguments needed: (remote) hostname, (remote) filepath, (local) path");
			System.exit(1);
		}

		try {
			String rname = "//" + args[0] + ":" + Registry.REGISTRY_PORT + "/remote";
			IfaceRemoteClass remote = (IfaceRemoteClass) Naming.lookup(rname);

			// Ensure there's something on the server we can copy.
			File remoteFile = new File(args[1]);
			if (!remote.exists(remoteFile.getName())) {
				byte[] content = "Never gonna give you up Never gonna let you down Never gonna run around and desert you Never gonna make you cry Never gonna say goodbye Never gonna tell a lie and hurt you".getBytes();
				remote.escribir(remoteFile.getName(), content.length, content);
			}

			// Copy from the server
			copy(remote, args[2], remoteFile.getName());

			// Send copy to server
			send_copy(remote, args[2], remoteFile.getName());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void send_copy(IfaceRemoteClass remote, String path, String name) {
		try {
			// prepare file and stream
			File file = new File(path, name);
			FileInputStream stream = new FileInputStream(file);

			byte[] local_content = new byte[(file.length() > Integer.MAX_VALUE) ? Integer.MAX_VALUE : (int) file.length()];
			int offset = 0;
			int writing_amount;
			do {
				// read local file in buffer
				stream.read(local_content);

				// set amount of bytes to write in the server
				writing_amount = (file.length() - offset < local_content.length)
					? (int)file.length() - offset
					: local_content.length;

				// ask the server to write
				offset += remote.escribir("copy_" + file.getName(), writing_amount, local_content);

			} while (offset < file.length());

			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void copy(IfaceRemoteClass remote, String path, String name) {
		try {
			// prepare file
			File file = new File(path, name);

			// read from server
			int offset = 0;
			IfaceRemoteFile result = remote.leer(file.getName(), offset, -1);

			if (result.isEmpty()) {
				return;
			}

			// if the file exists in the server and here, delete our file
			if (file.exists()) {
				file.delete();
			}

			// prepare output stream
			FileOutputStream stream = new FileOutputStream(file, true);

			// there might be more of that sweet data out there
			while (!result.isEmpty()) {

				// write the result to the local file
				offset += result.getSize();
				stream.write(result.getContent());

				// and try to read again, in case part of the file is missing
				result = remote.leer(file.getName(), offset, -1);
			}

			stream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
