package com.sync.note.test;

import static org.junit.Assert.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.sync.note.messages.Messages;
import com.sync.note.messages.SuperMessage;

public class SmokeTest {
	
	@Test
	public void SendRequestTest() {
		try {
			Thread.sleep(1000);
			Socket socket = new Socket("localhost", 8083);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			SuperMessage sm = new SuperMessage(Messages.OK_ANSWER);
			oos.writeObject(sm);
			oos.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}
	
	@Test
	@Ignore //This test should carry out a more specific functionality.
	public void SendRequestAnswerTest(){
		try {
			Thread.sleep(1000);
			Socket socket = new Socket("localhost", 8083);
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			SuperMessage sm = new SuperMessage(Messages.OK_ANSWER);
			oos.writeObject(sm);
			oos.flush();
			SuperMessage smreceived = (SuperMessage)ois.readObject();
			System.out.println("Received: " + smreceived.getMessageType());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}

}
