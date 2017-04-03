package Tests;

import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Connection;

import Software.Hashtag;
import Software.JDBC;
import VisibleForUser.Account;
import junit.framework.TestCase;
import no.hal.jex.runtime.JExercise;

public class JDBCTest extends TestCase {
	 
	
	private JDBC db; 
	
	@Override
	 protected void setUp() {
	    db = new JDBC();
	  }
	
	@JExercise(description = "Trying to connect to database")
	public void testConnection(){
		assertTrue(db.connect());
	}
	
	@JExercise(description = "Trying to insert Failure and checks whether allows incorrect Tag")
	public void testInsertfailure(){
		db.connect();
		assertTrue(db.insertFailure(0, 0, "TEST", 0, "TEST"));
		assertFalse(db.insertFailure(0, 0, "NULL", 0, "12345"));
		db.insertQuery("DELETE FROM Failures WHERE FE = 'TEST'"); 
	}
	
	@JExercise(description = "Insert feedback on link")
	public void testInsertFeedback(){
		db.connect();
		assertTrue(db.insertFeedback(1, 1, -1));
		db.insertQuery("DELETE FROM Feedback WHERE Rating = -1"); 
	}
	
	@JExercise(description = "Test whether getStudentID is ok")
	public void testGetStudentID(){
		db.connect();
		assertTrue(db.getStudentID() != -1); 
	}
	
	@JExercise(description = "Test whether getLinkID is ok")
	public void testGetLinkID(){
		db.connect();
		assertTrue(db.getLinkID("https://www.youtube.com/watch?v=ZHLdVRXIuC8") == 28); 
		assertTrue(db.getLinkID("test") == -1); 
	}
	
	@JExercise(description = "Test whether getLinks is ok")
	public void testGetLinks(){
		db.connect();
		ArrayList<String> links = db.getLinks("YouTube", "OOT"); 
		assertFalse(links.get(0).equals("")); 
		assertTrue(links.size()==3);
		
		links = db.getLinks("test", "OOT"); 
		assertTrue(links.get(0).equals("")); 
		assertTrue(links.size()==3);
		
		links = db.getLinks("YouTube", "test"); 
		assertTrue(links.get(0).equals("")); 
		assertTrue(links.size()==3);
		
	}
	
	@JExercise(description = "Test if getLastTag ok")
	public void testGetLastTag(){
		db.connect();
		db.insertFailure(0, 0, "TEST", 0, "TEST");
		
		
		assertTrue(db.getLastTag().equals("TEST")); 
		assertFalse(db.getLastTag().equals("")); 
		
		db.insertQuery("DELETE FROM Failures WHERE FE = 'TEST'");
	}
	
	@JExercise(description = "Test if limitReached ok")
	public void testLimitReached(){
		db.connect();
		
		db.insertFailure(0, 0, "TEST", 0, "TEST");
		assertFalse(db.limitReached("TEST", 0, 0));
		
		db.insertFailure(0, 0, "TEST", 0, "TEST");
		assertTrue(db.limitReached("TEST", 0, 0));

		db.insertQuery("DELETE FROM Failures WHERE FE = 'TEST'");
		db.insertQuery("DELETE FROM FailureLimit WHERE Tag = 'TEST'");
	}
	
	@JExercise(description = "Test if sendUserData ok")
	public void testSendUserData(){
		db.connect();
		
		assertTrue(db.sendUserData("TEST", "", "", "", "", "Lars Erik Kleiven"));
		db.insertQuery("DELETE FROM Users WHERE Username = 'TEST'");
	}
	
	@JExercise(description = "Test if getStudentAssistandID ok")
	public void testGetStudentAssistantID(){
		db.connect();
		
		assertTrue(db.getStudentAssistantID("Lars Erik Kleiven") == 3);
		assertFalse(db.getStudentAssistantID("Lars Erik Kleiven") != 3);
	}
	
	@JExercise(description = "Test if userExists ok")
	public void testUserExists(){
		db.connect();
		db.sendUserData("TEST", "", "", "", "", "Lars Erik Kleiven");
		assertTrue(db.userExists());
		db.insertQuery("DELETE FROM Users WHERE Username = 'TEST'");
	}
	
	@JExercise(description = "Test if getStudentAssistants ok")
	public void testGetUserAssistants(){
		db.connect();
		
		ArrayList<String> assistants = db.getStudentAssistants(); 
		assertTrue(assistants.size()>0); 
		assertTrue(assistants.indexOf("") == -1); 	

	}
	
	@JExercise(description = "Test if insertQuery ok")
	public void testQuery(){
		db.connect();
		
		assertTrue(db.insertQuery("DELETE FROM Users WHERE Username = 'TEST'"));
	}
	
	@JExercise(description = "Test getConnection")
	public void testGetConnection(){
		db.connect();
		
		assertTrue(db.getConnection() instanceof Connection);
	}
	
	
	@Override
	protected void tearDown(){
		db = null; 
	}

	  
}
