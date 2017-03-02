package GUI;

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
	
	@FXML
	private void initialize() {
		problemText.setText("Du sliter med ....");
		online3.setText("online3");
	}
	
	@FXML
	private void handleSend() {
		
		// Should send to DB
		// Should close the entire window
	}
	
	
}
