/*
* RemoteFile.java
* Class defining the response of the reading operation.
*
*/

package server;

import shared.IfaceRemoteFile;

class RemoteFile implements IfaceRemoteFile {

	private int size;
	private byte[] content;

	public RemoteFile() {
		super();
		this.size = 0;
		this.content = null;
	}

	public RemoteFile(int size, byte[] content) {
		super();
		this.size = size;
		this.content = content;
	}

	public boolean isEmpty() {
		return this.content == null || this.content.length == 0;
	}

	public int getSize() {
		return this.size;
	}

	public byte[] getContent() {
		return this.content;
	}
}
