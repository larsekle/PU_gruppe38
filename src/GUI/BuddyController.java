package GUI;

import java.util.ArrayList;
import java.util.Arrays;

import Software.JDBC;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;

public class BuddyController {
	
	private JDBC database; 
	
	@FXML
	private Text problemText;	
	
	@FXML
	private Hyperlink youtube1; 
	
	@FXML
	private Hyperlink youtube2; 
	
	@FXML
	private Hyperlink youtube3; 
	
	@FXML
	private Hyperlink wiki1; 
	
	@FXML
	private Hyperlink wiki2; 
	
	@FXML
	private Hyperlink wiki3; 
	
	@FXML
	private Hyperlink online1; 
	
	@FXML
	private Hyperlink online2; 
	
	@FXML
	private Hyperlink online3; 
	
	@FXML
	private Slider feedbackSlider; 
	
	
	private ArrayList<Hyperlink> hyperlinks = new ArrayList<Hyperlink>(Arrays.asList(wiki1, wiki2, wiki3, online1, online2, online3, youtube1, youtube2, youtube3));

	
	@FXML
	private void initialize() {
		database = new JDBC();
		database.connect();
		
		// Get hashtags from Failure table (?) 
		String hashtag = " ..... ";
		problemText.setText("Hei! Det ser ut som du sliter med " + hashtag + ". Jeg vil anbefale deg å ta en titt på følgende lenker: ");
		online3.setText("online3");
		
		// Get top links from database
		ArrayList<String> wikiLinks = database.getLinks("Wiki"); 
		ArrayList<String> youtubeLinks = database.getLinks("Youtube");
		ArrayList<String> onlineLinks = database.getLinks("Online");
		
		// Sets top links to the different tabs
		for (int i = 0; i<=3; i++){
			hyperlinks.get(i).setText(wikiLinks.get(i)); 
		}
		for (int i = 0; i<=3; i++){
			hyperlinks.get(i+3).setText(youtubeLinks.get(i)); 
		}
		for (int i = 0; i<=3; i++){
			hyperlinks.get(i+6).setText(onlineLinks.get(i)); 
		}
		
	}
	
	@FXML
	private void handleSend() {
		for (Hyperlink link : hyperlinks){
			if ((boolean) link.isVisited()){
				int linkID = database.getLinkID(link.getText());
				int studID = database.getStudentID();
				double rating = feedbackSlider.getValue();
				database.insertFeedback(linkID, studID, rating);
			}
		}
		// Should close the entire window
	}
	
	
}
