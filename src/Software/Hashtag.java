package Software;

import java.util.ArrayList;
import java.util.Arrays;
import GUI.Main;

/**
 * @author Lars E. Kleiven, Ingrid E. Hermanrud, Sigrid L. Fosen, Helena Van de Pontseele
 *
 */
public class Hashtag {
	
	private final int LIMIT_INCREMENT = 3;
	private JDBC database; 
	private boolean test = false;  
	private boolean alreadyActivated = false; 
	
	public static final ArrayList<String> TAGS = new ArrayList<String>(Arrays.asList("OOT", "interface", "inheritance", "pattern", "class", "vararg", "lambda", "functional interface", "type", "encapsulation", "valid state", "abstract class", "super class", "delegation", "observable", "anonymous class", "collection", "iteration", "text handling", "value types", "scanner", "arrayList", "compare", "IO", "casting")); 
	
	/**
	 * Constructor to set up database and connect
	 */
	public Hashtag (){
		this.database = new JDBC(LIMIT_INCREMENT);
	}  
	 
	/**
	 * Constructor for use in testing. Allows testing without starting the GUI. 
	 * @param test
	 */
	public Hashtag (boolean test){
		this.database = new JDBC(LIMIT_INCREMENT);
		this.test = test; 
	}
	
	/**
	 * Starts GUI if failure-limit is reached.
	 * @param tag
	 * @return whether method was used to talk to student or not.
	 */
	public boolean talkToStudent(String tag) {
		try{
			Main.main((String[]) null);
			alreadyActivated = true; 
			return true; 
		} catch (Exception e){
			return false; 
		}
	} 
	
	/**
	 * Sends failure from JUnit tests to database. 
	 * Accepts only valid tags and testing by use of tag 'TEST'.
	 * If test then GUI is not activated
	 * If already active, GUI will not be activated
	 * If failure-limit is reached and neither test nor already activated, GUI will be activated.  
	 * @param assignment
	 * @param exercise
	 * @param tag
	 * @param FE
	 * @return whether operation was successful or not. 
	 */
	public boolean sendToDB(int assignment, int exercise, String tag, String FE){
		if (TAGS.indexOf(tag) < 0 && !test){
			throw new IllegalArgumentException(tag + ":  Tag does not exist, contact supervisor"); 
		}
		database.insertFailure(assignment, exercise, tag, FE);
		if (database.limitReached(tag, assignment, exercise) && (!test && !alreadyActivated)){
			talkToStudent(tag);
		}
		return true; 
	}
	
	/**
	 * Get-method for database object. Allows developer to access database through the Hashtag class.
	 * @return database
	 */
	public JDBC getDatabase(){
		return database; 
	}
}
