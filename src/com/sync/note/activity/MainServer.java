package com.sync.note.activity;

import java.io.IOException;

import com.sync.note.servers.SyncNoteServer;
import com.sync.note.servers.SyncNoteServerImpl;

public class MainServer {

	
	/**
	 * This is the main program which will launch the server.
	 * This server will create a serversocket and expect petitions
	 * from client aplications. 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SyncNoteServer sns = new SyncNoteServerImpl(); // The server is created
		
		try {
			sns.runServer();		// The server is running.
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		// We prints the IOException.
		}
		
	}

}
