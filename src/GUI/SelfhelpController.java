package GUI;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import Software.Hashtag;
import Software.JDBC;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class SelfhelpController {
	
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
	
	@FXML 
	private Button t1;
	
	@FXML 
	private Text tag1; 

	@FXML 
	private Text tag2; 
	
	@FXML 
	private Text tag3; 
	
	@FXML 
	private Text tag4; 
	
	@FXML 
	private Text tag5; 
	
	@FXML 
	private Text tag6; 
	
	@FXML 
	private Text tag7; 
	
	@FXML 
	private Text tag8; 
	
	@FXML 
	private Text tag9; 
	
	@FXML 
	private Text tag10; 
	
	@FXML 
	private Text tag11; 
	
	@FXML 
	private Text tag12; 
	
	@FXML 
	private Text tag13; 

	@FXML 
	private Text tag14; 
	
	@FXML 
	private Text tag15; 
	
	@FXML 
	private Text tag16; 
	
	@FXML 
	private Text tag17; 
	
	@FXML 
	private Text tag18; 
	
	@FXML 
	private Text tag19; 
	
	@FXML 
	private Text tag20; 
	
	@FXML 
	private Text tag21; 
	
	@FXML 
	private Text tag22; 
	
	@FXML 
	private Text tag23; 
	
	@FXML 
	private Text tag24;
	
	@FXML 
	private Text tag25;
	
	@FXML 
	private Text tag26; 
	
	@FXML 
	private Text tag27; 
	
	@FXML 
	private Text tag28; 
	
	@FXML 
	private Text tag29; 
	
	@FXML 
	private Text tag30;
	
	@FXML 
	private ComboBox t2; 
		
	// Tried to collect Hyperlinks in ArrayList but continually failed when compiling. Could perhaps be looked at, but not important. 
	//private ArrayList<Hyperlink> hyperlinks = new ArrayList<Hyperlink>(Arrays.asList(wiki1, wiki2, wiki3, online1, online2, online3, youtube1, youtube2, youtube3));
	
	
	@FXML
	private void initialize() {
		database = new JDBC();
		database.connect();

		
		problemText.setText("Hi! BuddyBOT is here to help. Which topic would you like to take a look at?");
		online3.setText("online3");
		
		ArrayList<String> tags = Hashtag.TAGS;
		
		// Sort on tags without distinguish upper and lower case letters
		tags.sort(Comparator.comparing(tag -> tag.toLowerCase()));
		
		t2.getItems().removeAll(); 
		t2.getItems().addAll(tags); 
		
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
	// Gets value from drop down in GUI and request relevant links from DB
	private void linkImport(){
		
		String tag = t2.getValue().toString();
		
		problemText.setText("Ok! Then I would recommend taking a look at the following links: ");
		
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
