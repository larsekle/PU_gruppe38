package Tests;

import java.util.ArrayList;
import com.mysql.jdbc.Connection;
import Software.JDBC;
import junit.framework.TestCase;
import no.hal.jex.runtime.JExercise;

public class JDBCTest extends TestCase {
	 
	
	private static JDBC db = new JDBC(); 
	private static JDBC dbNoConnection = new JDBC(true); 
	
	@JExercise(description = "Trying to connect to database")
	public void testConnection(){
		assertTrue(db.connect());
	}
	
	@JExercise(description = "Trying to insert Failure and checks whether allows incorrect Tag")
	public void testInsertfailure(){
		assertTrue(db.insertFailure(0, 0, "TEST", "TEST"));
		assertFalse(db.insertFailure(0, 0, "NULL", "12345"));
		assertFalse(dbNoConnection.insertFailure(0, 0, "TEST", "TEST"));
		
		db.insertQuery("DELETE FROM Failures WHERE FE = 'TEST'"); 
	}
	
	@JExercise(description = "Insert feedback on link")
	public void testInsertFeedback(){
		assertTrue(db.insertFeedback(1, 1, -1, 1));
		assertFalse(dbNoConnection.insertFeedback(1, 1, -1, 1));

		db.insertQuery("DELETE FROM Feedback WHERE Rating = -1"); 
	}
	
	@JExercise(description = "Test whether getStudentID is ok")
	public void testGetStudentID(){
		assertTrue(db.getStudentID() != -1); 
		assertFalse(dbNoConnection.getStudentID() != -1); 
	}
	
	@JExercise(description = "Test whether getLinkID is ok")
	public void testGetLinkID(){
		assertTrue(db.getLinkID("https://www.youtube.com/watch?v=ZHLdVRXIuC8") == 28); 
		assertTrue(db.getLinkID("test") == -1); 
		assertTrue(dbNoConnection.getLinkID("test") == -1); 
	}
	 
	@JExercise(description = "Test whether getLinks is ok")
	public void testGetLinks(){
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
		db.insertFailure(0, 0, "TEST", "TEST");	
		
		assertTrue(db.getLastTag().equals("TEST")); 
		assertFalse(db.getLastTag().equals("")); 
		assertFalse(dbNoConnection.getLastTag().equals("TEST")); 
		
		db.insertQuery("DELETE FROM Failures WHERE FE = 'TEST'");
	}
	
	@JExercise(description = "Test if limitReached ok")
	public void testLimitReached(){		
		try {
			db.insertFailure(0, 0, "TEST", "TEST");
			assertFalse(db.limitReached("TEST", 0, 0));
			
			db.insertFailure(0, 0, "TEST", "TEST");
			assertTrue(db.limitReached("TEST", 0, 0));
			assertFalse(dbNoConnection.limitReached("TEST", 0, 0));

		} finally{
			db.insertQuery("DELETE FROM Failures WHERE FE = 'TEST'");
			db.insertQuery("DELETE FROM FailureLimit WHERE Tag = 'TEST'");
		}
	}
	
	@JExercise(description = "Test if sendUserData ok")
	public void testSendUserData(){	
		try {
			assertTrue(db.sendUserData("TEST", "", "", "", "", "Lars Erik Kleiven"));
			assertFalse(dbNoConnection.sendUserData("TEST", "", "", "", "", "Lars Erik Kleiven"));
		} finally {
			db.insertQuery("DELETE FROM Users WHERE Username = 'TEST'");
		}
	}
	
	@JExercise(description = "Test if getStudentAssistandID ok")
	public void testGetStudentAssistantID(){		
		assertTrue(db.getStudentAssistantID("Lars Erik Kleiven") == 1);
		assertFalse(db.getStudentAssistantID("Lars Erik Kleiven") != 1);
	}
	
	@JExercise(description = "Test if userExists ok")
	public void testUserExists(){
		db.sendUserData("TEST", "", "", "", "", "Lars Erik Kleiven");
		assertTrue(db.userExists());
		assertFalse(dbNoConnection.userExists());
		
		db.insertQuery("DELETE FROM Users WHERE Username = 'TEST'");
	}
	
	@JExercise(description = "Test if getStudentAssistants ok")
	public void testGetUserAssistants(){		
		ArrayList<String> assistants = db.getStudentAssistants(); 
		assertTrue(assistants.size() > 0); 
		assertTrue(assistants.indexOf("") == -1);

	}
	
	@JExercise(description = "Test if insertQuery ok")
	public void testQuery(){		
		assertTrue(db.insertQuery("DELETE FROM Users WHERE Username = 'TEST'"));
		assertFalse(dbNoConnection.insertQuery("DELETE FROM Users WHERE Username = 'TEST'"));
		
	}
	
	@JExercise(description = "Test getConnection")
	public void testGetConnection(){		
		assertTrue(db.getConnection() instanceof Connection);
	}

	  
}
