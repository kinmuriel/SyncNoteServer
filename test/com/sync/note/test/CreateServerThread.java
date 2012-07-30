package com.sync.note.test;

import java.io.IOException;

import com.sync.note.servers.SyncNoteServer;
import com.sync.note.servers.SyncNoteServerImpl;

public class CreateServerThread extends Thread{
	@Override
	public void run() {
		// TODO Auto-generated method stub
		SyncNoteServer sns = new SyncNoteServerImpl();
		
		try {
			System.out.println("Creating Server");
			sns.runServer();
			System.out.println("Server Created");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		// We prints the IOException.
		}
		
	}
}