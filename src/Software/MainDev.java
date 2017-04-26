package Software;

import java.util.Arrays;
import java.util.List;

import GUI.Main;
import javafx.stage.Stage;

public class MainDev extends junit.framework.TestCase {

	
//	 Script to insert random rows into Failures table. Disable the TalkToStudent feature before executing 
	public static void main(String[] args) {
		
		
		List<String> tags = Arrays.asList("OOT", "interface", "inheritance", "pattern", "class", "vararg", "lambda", "functional interface", "type", "encapsulation", "valid state", "abstract class", "super class", "delegation", "observable", "anonymous class", "collection", "iteration", "text handling", "value types", "scanner", "arrayList", "compare", "IO", "casting"); 
		List<String> FE = Arrays.asList("Failure", "Error"); 
		Hashtag ht = new Hashtag();	
		
		for (int i = 0 ; i<10; i++){
			int assignment = (int) Math.ceil(Math.random()*4);
//			int exercise = (int) Math.ceil(Math.random()*4); 
//			int fe = (int) Math.ceil(Math.random()*2-1);		
			int linkID = (int) Math.ceil(Math.random()*7+138); 
			int rating = (int) Math.ceil(Math.random()*3+2); 
			ht.getDatabase().insertFeedback(138, 1, rating, assignment);
//			ht.getDatabase().insertFailure(assignment, exercise, tags.get(tag), FE.get(fe)); 
		}
	} 
	
}
