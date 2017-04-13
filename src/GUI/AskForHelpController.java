package GUI;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import Software.Hashtag;
import Software.JDBC;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Lars E. Kleiven, Ingrid E. Hermanrud, Sigrid L. Fosen, Helena Van de Pontseele
 *
 */
public class AskForHelpController {
	
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
	private Button send; 
	
	@FXML 
	private ComboBox<String> linkSelector; 
	
	/**
	 * Method to initialize FXML window. Gets all tags and presents them to the user through a drop-down list.
	 * If test then test sequence will be run directly. 
	 */
	@FXML
	private void initialize() {
		database = new JDBC();
			
		problemText.setText("Hi! BuddyBOT is here to help. Which topic would you like to take a look at?");	
		ArrayList<String> tags = Hashtag.TAGS;
		
		// Sort on tags without distinguish upper and lower case letters
		tags.sort(Comparator.comparing(tag -> tag.toLowerCase()));
		
		linkSelector.getItems().removeAll(); 
		linkSelector.getItems().addAll(tags); 
		try{
			if (RegisterMain.test){
				linkSelector.setValue(tags.get(0));
				linkImport(); 
				youtube1.setVisited(true);
				handleSend();
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		
	}
	
	/**
	 * When student click on a link, they will be redirected to the webpage through default browser. 
	 * Link is then set to default, so that the user can access multiple links at once.  
	 */
	@FXML
	private void handleLink(){
		for (Hyperlink link : Arrays.asList(wiki1, wiki2, wiki3, online1, online2, online3, youtube1, youtube2, youtube3)){
			if ((boolean) link.isVisited()){
				try {
			    	java.awt.Desktop.getDesktop().browse(new URI(link.getText()));
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
	 * When a link is selected this method will import the relevant links and set them to GUI.
	 */
	@FXML 
	private void linkImport(){
		String tag = linkSelector.getValue().toString();
		
		
		problemText.setText("Ok! Then I would recommend taking a look at the following links: ");
		
		ArrayList<String> wikiLinks = database.getLinks("Wiki", tag); 
		ArrayList<String> youtubeLinks = database.getLinks("Youtube", tag);
		ArrayList<String> onlineLinks = database.getLinks("Online", tag);
		
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
	
	/**
	 * Sends feedback from students to database based on link visited and rating through slide bar
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
			new Main().start(stage);
		} 
	}
}
