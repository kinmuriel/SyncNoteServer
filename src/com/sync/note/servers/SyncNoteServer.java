package com.sync.note.servers;

import java.io.IOException;

public interface SyncNoteServer {

	/**
	 * This method starts the SyncNote server, waiting for petitions and serving them.
	 */
	void runServer() throws IOException;
	
}
