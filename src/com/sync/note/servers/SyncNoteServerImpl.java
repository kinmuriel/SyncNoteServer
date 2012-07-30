package com.sync.note.servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SyncNoteServerImpl implements SyncNoteServer{

	private static int PORT = 8083;
	
	/**
	 * This method creates a thread that handles the petition
	 * 
	 * 
	 * @param socket
	 */
	private void createThreadServer(Socket socket){
		Thread hpt = new Thread((new HandlerPetitionThread(socket)));
		hpt.start();
	}
	
	@Override
	public void runServer() throws IOException {
		// TODO Auto-generated method stub
		ServerSocket ss = new ServerSocket(PORT);
		
		while(true){
			Socket socket = ss.accept();
			createThreadServer(socket);
		}
	}
	
}
