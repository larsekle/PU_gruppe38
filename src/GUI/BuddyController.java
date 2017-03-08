package GUI;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import Software.JDBC;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import javafx.stage.Stage;

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
	
	@FXML 
	private Button Send; 
	
	// Tried to collect Hyperlinks in ArrayList but continually failed when compiling. Could perhaps be looked at, but not important. 
	//private ArrayList<Hyperlink> hyperlinks = new ArrayList<Hyperlink>(Arrays.asList(wiki1, wiki2, wiki3, online1, online2, online3, youtube1, youtube2, youtube3));
	
	
	@FXML
	private void initialize() {
		database = new JDBC();
		database.connect();
		int tag = database.getLastTag(); 
		
		
		// Temporarily for use in tests
		tag = 0; 
		
		
		// Get hashtags from Failure table (?) 
		String hashtag = " ..... ";
		problemText.setText("Hi! It looks like you are strugglig with #" + tag + ". I would advise you to look at the following resouces:");
		online3.setText("online3");
		
		// Get top links from database
		ArrayList<String> wikiLinks = database.getLinks("Wiki", tag); 
		ArrayList<String> youtubeLinks = database.getLinks("Youtube", tag);
		ArrayList<String> onlineLinks = database.getLinks("Online", tag);
		
		// Sets top links to the different tabs
		wiki1.setText(wikiLinks.get(0));
		wiki2.setText(wikiLinks.get(1));
		wiki3.setText(wikiLinks.get(2));
		
		online1.setText(onlineLinks.get(0));
		online2.setText(onlineLinks.get(1));
		online3.setText(onlineLinks.get(2));
		
		youtube1.setText(youtubeLinks.get(0));
		youtube2.setText(youtubeLinks.get(1));
		youtube3.setText(youtubeLinks.get(2));
		
	}
	
	@FXML
	private void handleLink(){
		for (Hyperlink link : Arrays.asList(wiki1, wiki2, wiki3, online1, online2, online3, youtube1, youtube2, youtube3)){
			if ((boolean) link.isVisited()){
				try {
			    	java.awt.Desktop.getDesktop().browse(new URI(link.getText()));
			    } catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				link.setVisited(false);
			}
		}
	}
	
	@FXML
	// Sends review from students to database based on link visited and rating through slide bar
	private void handleSend() {
		for (Hyperlink link : Arrays.asList(wiki1, wiki2, wiki3, online1, online2, online3, youtube1, youtube2, youtube3)){
			if ((boolean) link.isVisited()){
				int linkID = database.getLinkID(link.getText());
				int studID = database.getStudentID();
				double rating = feedbackSlider.getValue();
				database.insertFeedback(linkID, studID, rating);
				link.setVisited(false);
			}
		}
		Stage stage = (Stage) Send.getScene().getWindow();
		stage.close();
	}
}
