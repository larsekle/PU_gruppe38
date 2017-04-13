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

/**
 * @author Lars E. Kleiven, Ingrid E. Hermanrud, Sigrid L. Fosen, Helena Van de Pontseele
 *
 */
public class BuddyController {
	
	private JDBC database; 
	
	@FXML
	private Text message;	
	
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
	private Button send; 
	
	private String tag; 
	
	/**
	 * Method to initialize FXML window. Gets last used tag, and gets associated links from database. 
	 * If test then 'encapsulation' is used as default tag to test functionality. 
	 * If test then test sequence will be run directly. 
	 */
	@FXML
	private void initialize() {
		database = new JDBC();
		
		tag = database.getLastTag(); 
		if (RegisterMain.test) tag = "encapsulation"; 

		message.setText("Hi! It looks like you are strugglig with the topic " + tag + ". I would advise you to look at the following resouces:");
		
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
		
		try{
			if (RegisterMain.test){
				youtube1.setVisited(true);
				handleSend();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
	} 
	
	/**
	 * When student click on a link, they will be redirected to the webpage through default browser. 
	 * Link is then set to default. 
	 */
	@FXML
	private void handleLink(){
		for (Hyperlink link : Arrays.asList(wiki1, wiki2, wiki3, online1, online2, online3, youtube1, youtube2, youtube3)){
			if ((boolean) link.isVisited()){
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
	 
	/**
	 * Sends feedback from students to database based on link visited and rating through slide bar.
	 * Uses try/catch to handle testing. 
	 */
	@FXML
	private void handleSend() {
		for (Hyperlink link : Arrays.asList(wiki1, wiki2, wiki3, online1, online2, online3, youtube1, youtube2, youtube3)){
			if ((boolean) link.isVisited()){
				int linkID = database.getLinkID(link.getText());
				int studID = database.getStudentID();
				int rating = (int) feedbackSlider.getValue();
				int assignment = database.getLastAssignment(); 
				database.insertFeedback(linkID, studID, rating, assignment);
				link.setVisited(false);
			}
		}
		Stage stage = null; 
		try {
			stage = (Stage) send.getScene().getWindow();
			stage.close();
		}catch (Exception e){
			stage = RegisterMain.stage; 
		} 
	}
}
