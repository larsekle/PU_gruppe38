package Tests;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import GUI.AskForHelp;
import GUI.Main;
import junit.framework.TestCase;
import no.hal.jex.runtime.JExercise;

public class GUITest extends TestCase {
	ExecutorService service = Executors.newSingleThreadExecutor(); 
	
	@JExercise(description = "Trying to run Main and then AskForHelp from the Main method. ExecuteService helps terminate if process takes to long.")
	public void testGUI(){			
		
		try{
			Main.test = true;
			AskForHelp.test = true; 
			service.execute(new Main());
			service.shutdown();
			service.awaitTermination(10, TimeUnit.SECONDS);
			assertTrue(true); 
			} catch(Exception e){
			fail(); 
		}
		
	}	
}
