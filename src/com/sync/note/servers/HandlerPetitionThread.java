package com.sync.note.servers;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.sync.note.messages.SuperMessage;
/**
 * Class responsible for handles different petitions and answer 
 * depending the different kind of message which is received.
 * 
 * @author kinmuriel
 *
 */
public class HandlerPetitionThread implements Runnable{

	private Socket socket;
	
	public HandlerPetitionThread(Socket s) {
		// TODO Auto-generated constructor stub
		socket = s;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			SuperMessage sm = (SuperMessage)ois.readObject();
			System.out.println("MessageType: " + sm.getMessageType());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
