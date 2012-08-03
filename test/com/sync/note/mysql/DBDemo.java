package com.sync.note.mysql;

import static org.junit.Assert.*;

import java.sql.*;
import java.util.Properties;

import org.junit.Test;

public class DBDemo
{
  // The JDBC Connector Class.
  private static final String dbClassName = "com.mysql.jdbc.Driver";

  // Connection string. emotherearth is the database the program
  // is connecting to. You can include user and password after this
  // by adding (say) ?user=paulr&password=paulr. Not recommended!

  private static final String CONNECTION =
                          "jdbc:mysql://127.0.0.1/emotherearth";

  @Test
  public void BasicGoodConnectionTest() throws
                             ClassNotFoundException,SQLException
  {
    System.out.println(dbClassName);
    // Class.forName(xxx) loads the jdbc classes and
    // creates a drivermanager class factory
    Class.forName(dbClassName);

    // Properties for user and password. Here the user and password are both 'paulr'
    Properties p = new Properties();
    p.put("user","test");
    p.put("password","1234");

    // Now try to connect
    Connection c = DriverManager.getConnection(CONNECTION,p);

    System.out.println("It works !");
    c.close();
    }
  
  @Test
  public void BasicWrongConnectionTest() throws ClassNotFoundException{
	  System.out.println(dbClassName);
	    // Class.forName(xxx) loads the jdbc classes and
	    // creates a drivermanager class factory
	  Class.forName(dbClassName);
	  
	// Properties for user and password. Here the user and password are both 'paulr'
	    Properties p = new Properties();
	    p.put("user","bad");
	    p.put("password","1234");

	    // Now try to connect
	    try{
	    	Connection c = DriverManager.getConnection(CONNECTION,p);
	    	c.close();
	    	fail("It should have failed cause there is no user named bad");
	    }catch(SQLException sql){
	    	//Nothing to do.
	    }
  	}
  
  @Test
  public void BasicWrongPassConnection() throws ClassNotFoundException{
	  Class.forName(dbClassName);
	  
	  Properties p = new Properties();
	  p.put("user", "test");
	  p.put("password", "123");
	  
	  try{
		  Connection c = DriverManager.getConnection(CONNECTION,p);
		  c.close();
		  fail("It should have failed cause wrong password");
	  }catch(SQLException s){
		  //Nothing to do.
	  }
  }
}