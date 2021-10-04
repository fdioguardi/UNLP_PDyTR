/*
* IfaceRemoteFile.java
* Interface defining the response object of the reading operation.
*
*/

package shared;

import java.io.Serializable;

public interface IfaceRemoteFile extends Serializable {

	public boolean isEmpty();
	public int getSize();
	public byte[] getContent();
}
