package Tests;

import Software.Hashtag;
import Software.JDBC;
import junit.framework.TestCase;
import no.hal.jex.runtime.JExercise;

public class HashtagTest extends TestCase {
	
	private static Hashtag hash = new Hashtag(true); 
	private static JDBC database = hash.getDatabase(); 
	
	@JExercise(description = "Trying to connect to database")
	public void testSendToDB(){		

		hash.sendToDB(0, 0, "TEST", "TEST");
		database.insertQuery("DELETE FROM Failures WHERE Tag = 'TEST'"); 
		database.insertQuery("DELETE FROM FailureLimit WHERE Tag = 'TEST'"); 
	}
		
}
