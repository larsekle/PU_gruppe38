package GUI;

import java.awt.Desktop;
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
	
	private String tag; 
	
	@FXML
	private void initialize() {
		database = new JDBC();
		database.connect();
		
		if (!Main.test)  tag = database.getLastTag(); 
		else tag = "encapsulation"; 
		
		problemText.setText("Hi! It looks like you are strugglig with the topic " + tag + ". I would advise you to look at the following resouces:");
		
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
	// When student click on a link, they will be redirected to the webpage through default browser
	private void handleLink(){
		for (Hyperlink link : Arrays.asList(wiki1, wiki2, wiki3, online1, online2, online3, youtube1, youtube2, youtube3)){
			if ((boolean) link.isVisited() && !Main.test){
				try {
			    	Desktop.getDesktop().browse(new URI(link.getText()));
			    } catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (IOException e) { 
					e.printStackTrace();
				}
				link.setVisited(false);
			}
		}
	}
	 
	@FXML
	// Sends feedback from students to database based on link visited and rating through slide bar
	private void handleSend() {
		for (Hyperlink link : Arrays.asList(wiki1, wiki2, wiki3, online1, online2, online3, youtube1, youtube2, youtube3)){
			if ((boolean) link.isVisited()){
				int linkID = database.getLinkID(link.getText());
				int studID = database.getStudentID();
				int rating = (int) feedbackSlider.getValue();
				if (!Main.test) {
					int assignment = database.getLastAssignment(); 
					database.insertFeedback(linkID, studID, rating, assignment);
				}
				link.setVisited(false);
			}
		}
		if (!Main.test){
			Stage stage = (Stage) Send.getScene().getWindow();
			stage.close();
		}
	}
}
