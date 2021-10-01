/*
* RemoteFile.java
* Class defining the response of the reading operation.
*
*/

import java.io.Serializable;

public class RemoteFile implements Serializable {

	private int size;
	private byte[] content;

	public RemoteFile(int size, byte[] content) {
		super();
		this.size = size;
		this.content = content;
	}

	public int getSize() {
		return this.size;
	}

	public byte[] getContent() {
		return this.content;
	}
}
