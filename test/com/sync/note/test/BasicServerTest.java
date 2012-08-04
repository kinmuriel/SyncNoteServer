package com.sync.note.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sync.note.messages.Messages;
import com.sync.note.messages.RegisterMessage;
import com.sync.note.messages.SuperMessage;

public class BasicServerTest {

	private static final String dbClassName = "com.mysql.jdbc.Driver";
	private static final String CONNECTION =
            "jdbc:mysql://127.0.0.1/SyncNoteServer";
	private static final String USER_DATABASE = "SyncNoteUser";
	private static final String PASSWORD_DATABASE = "1234";
	
	@BeforeClass
	public static void createServer(){
		Thread thread = new CreateServerThread();
		thread.start();
	}
	
	
	@Test
	public void goodRegisterTest() throws UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
		Thread.sleep(1000);
		Socket socket = new Socket("localhost", 8083);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		RegisterMessage rm = new RegisterMessage("GoodRegisterTest", "pass1");
		System.out.println("RegisterMessage sending");
		oos.writeObject(rm);
		System.out.println("RegisterMessage sended. Waiting for answer");
		SuperMessage sm = (SuperMessage)ois.readObject();
		if(sm.getMessageType() != Messages.GOOD_REGISTER_ANSWER){
			fail("There have been some mistake with register petition message");		
		}
	}
	
	@Test
	public void badRegisterTest() throws UnknownHostException, IOException, InterruptedException, ClassNotFoundException {
		Thread.sleep(1000);
		Socket socket = new Socket("localhost", 8083);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		RegisterMessage rm = new RegisterMessage("GoodRegisterTest", "pass1");
		System.out.println("RegisterMessage sending");
		oos.writeObject(rm);
		System.out.println("RegisterMessage sended. Waiting for answer");
		SuperMessage sm = (SuperMessage)ois.readObject();
		if(sm.getMessageType() != Messages.BAD_REGISTER_ANSWER){
			fail("There should have been some mistake with register petition message, cause " +
					"the user should exist with the previous test.");		
		}
	}
	
	@AfterClass
	public static void deleteUser() throws ClassNotFoundException, SQLException{
		
		Class.forName(dbClassName);
		
		Properties p = new Properties();
	    p.put("user",USER_DATABASE);
	    p.put("password",PASSWORD_DATABASE);

	    // Now try to connect
	    Connection c = DriverManager.getConnection(CONNECTION,p);
		
	    Statement statement = c.createStatement();
	    String sql = "DELETE FROM users where username = 'GoodRegisterTest'";
	    statement.execute(sql);
	}

}
