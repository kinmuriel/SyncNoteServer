package com.sync.note.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class BasicServerTest {

	@BeforeClass
	public static void createServer(){
		Thread thread = new CreateServerThread();
		thread.start();
	}
	
	
	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
