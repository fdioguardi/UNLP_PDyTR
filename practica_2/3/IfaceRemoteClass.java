/*
* IfaceRemoteClass.java
* Interface defining only one method which can be invoked remotely
*
*
* Needed for defining remote method/s
*/

import java.rmi.Remote;
import java.rmi.RemoteException;

/* This interface will need an implementing class */
public interface IfaceRemoteClass extends Remote {

	public RemoteFile leer(String name, int offset, int reading_amount) throws RemoteException;
	public int escribir(String name, int writing_amount, byte[] data) throws RemoteException;
}