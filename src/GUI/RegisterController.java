package GUI;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import Software.JDBC;
import VisibleForUser.AskForHelp;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Lars E. Kleiven, Ingrid E. Hermanrud, Sigrid L. Fosen, Helena Van de Pontseele
 *
 */
public class RegisterController {
	
	private JDBC database; 
	
	@FXML
	private AnchorPane pane; 
	
	@FXML
	private TextField username;	
	
	@FXML
	private TextField password;
	
	@FXML
	private TextField confirmPassword;
	
	@FXML
	private TextField firstName;
	
	@FXML
	private TextField lastName;
	
	@FXML
	private TextField emailAddress;
	
	@FXML
	private CheckBox termsAccepted; 
	
	@FXML 
	private Button send; 
	
	@FXML 
	private Hyperlink buddyWebPage;
	
	@FXML
	private Hyperlink termsConditions;

	@FXML
	private Text message; 
	
	@FXML
	private ComboBox<String> studentAss; 
		
	
	/**
	 * Method to initialize FXML window. 
	 * Gets all student assistants from database, and shows them in a drop-down list. 
	 * If test then test sequence will be run directly. 
	 */
	@FXML
	private void initialize() {
		database = new JDBC(); 
		
		studentAss.getItems().removeAll(); 
		studentAss.getItems().addAll(database.getStudentAssistants()); 
		
		try{
			if (RegisterMain.test){
				message.setText("This is a test");
				handleSend();
				username.setText("TEST");
				handleSend();
				password.setText("TEST");
				handleSend();
				password.setText("TEST");
				confirmPassword.setText("TEST");
				handleSend();
				firstName.setText("TEST");
				handleSend();
				lastName.setText("TEST");
				handleSend();
				emailAddress.setText("TEST@");
				handleSend();
				termsAccepted.setSelected(true);
				handleSend(); 
				studentAss.setValue(database.getStudentAssistants().get(0));
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
		for (Hyperlink link : Arrays.asList(buddyWebPage, termsConditions)){
			if ((boolean) link.isVisited()){
				try {
			    	if (link.getText().equals("terms & conditions")){
			    		java.awt.Desktop.getDesktop().browse(new URI("http://folk.ntnu.no/sigrif/terms"));
			    	} else{
			    		java.awt.Desktop.getDesktop().browse(new URI(link.getText()));
			    	}
			    } catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	/**
	 * Sends feedback from students to database based on link visited and rating through slide bar.
	 * Check whether input is correct or not. Asks user to provide correct input if wrong. 
	 * Uses try/catch to handle testing. 
	 * Opens either selfhelp or regular BuddyBOT based on whether it was activated by a failure-insert or manually.
	 */
	@FXML
	private void handleSend() {		
		if (username.getText().equals("Username") || password.getText().equals("Password") || confirmPassword.getText().equals("Confirm password") || firstName.getText().equals("First name") || lastName.getText().equals("Last name")  || emailAddress.getText().equals("Email address")){
			message.setText("Please fill ut the entire form");
		} else if (!password.getText().equals(confirmPassword.getText())){
			password.setText("Password");
			confirmPassword.setText("Comfirm password");
			message.setText("Password and confirmed password are not the same");
		} else if(!emailAddress.getText().contains("@")){
			emailAddress.setText("Email address");
			message.setText("Please enter a valid email address");
		} else if(!termsAccepted.isSelected()){
			message.setText("Please read and accept our terms & conditions");
		} else if(studentAss.getValue() == null){
			message.setText("Please select your student assistant");
		} else{
			String user = username.getText(); 
			String pass = password.getText(); 
			String first = firstName.getText(); 
			String last = lastName.getText(); 
			String email = emailAddress.getText(); 
			String studass = studentAss.getValue().toString(); 
			
			Stage stage = null; 
			try {
				stage = (Stage) send.getScene().getWindow();
			} catch (Exception e){
				stage = RegisterMain.stage; 
				new AskForHelp().start(stage);
			} 
			
			if (database.sendUserData(user, pass, first, last, email, studass) && !RegisterMain.test){
				if (database.getLastTag().equals("-1")){
					new AskForHelp().start(stage);
				} else{
					new Main().start(stage);					
				}
			} else{
				stage.close();
			}
		}
	}
}
