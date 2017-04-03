package Software;

import java.util.Arrays;
import java.util.List;

public class HashtagTest extends junit.framework.TestCase {

	protected void setUp(){
		
	}
	
	// Script to insert random rows into Failures table. Disable the TalkToStudent feature before executing 
	public static void main(String[] args) {
		
		List<String> tags = Arrays.asList("OOT", "interface", "inheritance", "pattern", "class", "vararg", "lambda", "functional interface", "type", "encapsulation", "valid state", "abstract class", "super class", "delegation", "observable", "anonymous class", "collection", "iteration", "text handling", "value types", "scanner", "arrayList", "compare", "IO", "casting"); 
		List<String> FE = Arrays.asList("Failure", "Error"); 
		Hashtag ht = new Hashtag();
		
		
		ht.getDatabase().connect();
		
		
		for (int i = 28; i<121; i++){
			int studID = 1; 
			int linkID = i;
			int rating = (int) Math.ceil(Math.random()*5); 
			
			ht.getDatabase().insertFeedback(linkID, studID, rating);
			
		}
	}
	
}
