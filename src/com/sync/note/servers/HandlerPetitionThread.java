package com.sync.note.servers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.sync.note.messages.LoginMessage;
import com.sync.note.messages.RegisterMessage;
import com.sync.note.messages.SuperMessage;
import com.sync.note.messages.Messages;
/**
 * Class responsible for handles different petitions and answer 
 * depending the different kind of message which is received.
 * 
 * @author kinmuriel
 *
 */
public class HandlerPetitionThread implements Runnable{

	private static final String dbClassName = "com.mysql.jdbc.Driver";
	private static final String CONNECTION =
            "jdbc:mysql://127.0.0.1/SyncNoteServer";
	private static final String USER_DATABASE = "SyncNoteUser";
	private static final String PASSWORD_DATABASE = "1234";
	
	private Socket socket;
	
	public HandlerPetitionThread(Socket s) {
		// TODO Auto-generated constructor stub
		socket = s;
	}
	
	private boolean existUser(String userName, Connection c){
		try {
			System.out.println("Creating statement...");
			Statement stmnt = c.createStatement();
			String sql = "SELECT * FROM users";
		    ResultSet rs = stmnt.executeQuery(sql);
		    return rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return true;//If some mistake has taken place during execution, we return true 
					// cause not to create any new user.
		}
	}
	
	private boolean existUser(String userName, String password, Connection c){
		boolean returnValue = false;
		try{
			System.out.println("Creating statement...");
			Statement stmnt = c.createStatement();
			String sql = "SELECT * FROM users WHERE username = '" + userName + "' and " +
					"password = '" + password + "'";
			ResultSet rs = stmnt.executeQuery(sql);
		    return rs.next();
		}catch(SQLException e){
			e.printStackTrace();
		}
		
		return returnValue;
		
	}
	
	private boolean insertUser(Connection c, String userName, String password){
		boolean returnValue = false;
		
		System.out.println("Inserting records into the table...");	      
		try {
			Statement stmt = c.createStatement();
			String sql = "INSERT INTO users " +
	                   "VALUES ('" + userName + "','" + password + "')";
			stmt.executeUpdate(sql);
			returnValue = true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return returnValue;
	}
	
	private Connection createConnection() throws SQLException, ClassNotFoundException{
		Class.forName(dbClassName);
		Properties p = new Properties();
	    p.put("user",USER_DATABASE);
	    p.put("password",PASSWORD_DATABASE);

	    // Now try to connect
	    System.out.println("Connecting to a selected database...");
	    Connection c = DriverManager.getConnection(CONNECTION,p);
	    System.out.println("Connected database successfully...");
	    
	    return c;
	}
	
	private void sendBasicAnswer(ObjectOutputStream oos, int message) throws IOException{
		SuperMessage sm = new SuperMessage(message);
		oos.writeObject(sm);
	}
	
	private void processLoginRequestMessage(LoginMessage sm, ObjectOutputStream oos) throws SQLException, ClassNotFoundException, IOException{
		Connection c = createConnection();
		
		String username = sm.getUserName();
		String password = sm.getPassword();
		
		if(existUser(username,password,c)){
			System.out.println("User with password found. Sending back good message");
			sendBasicAnswer(oos, Messages.GOOD_LOGIN_ANSWER);
		}else{
			System.out.println("The login or user is not correct. Sending back bad message");
			sendBasicAnswer(oos, Messages.BAD_LOGIN_ANSWER);
		}
	}
	
	private void processRegisterRequestMessage(RegisterMessage sm, ObjectOutputStream oos) throws ClassNotFoundException, SQLException, IOException{
		Connection c = createConnection();
	    
	    String username = sm.getUserName();
	    String password = sm.getPassword();
	    
	    if(!existUser(username,c)){
	    	if(!insertUser(c,username,password)){
	    		System.out.println("An error has taken place. Sending back bad message");
	    		sendBasicAnswer(oos,Messages.BAD_REGISTER_ANSWER);
	    	}else{
	    		System.out.println("Everything has taken place correctly. Sending back good message");
	    		sendBasicAnswer(oos,Messages.GOOD_REGISTER_ANSWER);
	    	}
	    }else{
	    	System.out.println("The user exits. Sending back bad message");
	    	sendBasicAnswer(oos,Messages.BAD_REGISTER_ANSWER);
	    }
	}
	
	private void processMessage(SuperMessage sm,ObjectOutputStream oos) throws IOException{
		
		try{
			System.out.println("Message type received :" + sm.getMessageType());
			switch(sm.getMessageType()){
				case Messages.LOGIN_REQUEST:
					LoginMessage lm = (LoginMessage)sm;
					processLoginRequestMessage(lm,oos);
					break;
				case Messages.REGISTER_REQUEST:
					RegisterMessage rm = (RegisterMessage)sm;
					processRegisterRequestMessage(rm,oos);
					break;
				/*
				 * Others messages: Update_request
				 * 					Password_change_request
				 * 					Create_note_request
				 * 
				 */
			}
		}catch(ClassNotFoundException c){
			c.printStackTrace();
			System.out.println("Internal error. Proceding to inform client part");
			sendBasicAnswer(oos, Messages.INTERNAL_ERROR);
		}catch(SQLException s){
			s.printStackTrace();
			System.out.println("Internal error. Proceding to inform client part");
			sendBasicAnswer(oos, Messages.INTERNAL_ERROR);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Internal error. Proceding to inform client part");
			sendBasicAnswer(oos, Messages.INTERNAL_ERROR);
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			SuperMessage sm = (SuperMessage)ois.readObject();
			processMessage(sm,oos);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
