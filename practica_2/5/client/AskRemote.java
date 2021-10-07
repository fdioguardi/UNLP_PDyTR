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

			long start, end;
			for (int i = 0; i < 100000; i++) {
				start = System.nanoTime();
				remote.blank();
				end = System.nanoTime();

				System.out.println((end - start) / 1000000.0);
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
