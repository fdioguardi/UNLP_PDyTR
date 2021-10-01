/*
* RemoteClass.java
* Just implements the RemoteMethod interface as an extension to
* UnicastRemoteObject
*
*
* Needed for implementing remote method/s
*/

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;

/* This class implements the interface with remote methods */
public class RemoteClass extends UnicastRemoteObject implements IfaceRemoteClass {

	private String database = "files";

	protected RemoteClass() throws RemoteException {
		super();
	}

	public RemoteFile leer(String name, int offset, int reading_amount) throws RemoteException {


		try {
			File file = new File(this.database + File.separator + name);
			FileInputStream stream = new FileInputStream(file);

			byte[] result = new byte[(file.length() - offset >= reading_amount) ? reading_amount : (int)(file.length() - offset) ];

			stream.read(result, offset, result.length);
			stream.close();

			return new RemoteFile(result.length, result);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public int escribir(String name, int writing_amount, byte[] data) throws RemoteException {
		try {
			File file = new File(this.database + File.separator + name);
			FileOutputStream stream = new FileOutputStream(file, true);

			stream.write(data, 0, writing_amount);
			stream.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return writing_amount;
	}
}
