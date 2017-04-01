package Tests;

import java.sql.SQLException;

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
	}
	
	@Override
	protected void tearDown(){
		db = null; 
	}

	  
}
