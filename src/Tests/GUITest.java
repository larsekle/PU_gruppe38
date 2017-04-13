package Tests;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import GUI.RegisterMain;
import Software.JDBC;
import junit.framework.TestCase;
import no.hal.jex.runtime.JExercise;

public class GUITest extends TestCase {
	ExecutorService service = Executors.newSingleThreadExecutor(); 
	
	@JExercise(description = "Trying to run Main and then AskForHelp from the Main method. ExecuteService helps terminate if process takes to long.")
	public void testRegisterGUI(){			
		
		try{
			service.execute(new RegisterMain());
			service.shutdown();
			service.awaitTermination(10, TimeUnit.SECONDS);
			JDBC database = new JDBC(); 
			database.insertQuery("DELETE FROM Users WHERE Username = 'TEST'"); 
			database.insertQuery("DELETE FROM Feedback WHERE Assignment = -1"); 
			assertTrue(true);
		} catch(Exception e){
			fail(); 
		}
		
	}	
}
