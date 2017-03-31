package GUI;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;

import Software.JDBC;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RegisterController {
	
	private JDBC database; 
	
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
	
	@FXML
	private void initialize() {
		JDBC database = new JDBC(); 
		database.connect();
		
		studentAss.getItems().removeAll(); 
		studentAss.getItems().addAll(database.getStudentAssistants()); 
		
		
	}
	
	@FXML
	// When student click on a link, they will be redirected to the webpage through default browser
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
	
	@FXML
	// Sends review from students to database based on link visited and rating through slide bar
	private void handleSend() {
		JDBC database = new JDBC(); 
		database.connect();
		
		
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
		} else if(studentAss.getValue().isEmpty()){
			message.setText("Please select your student assistant");
		} else{
			String user = username.getText(); 
			String pass = password.getText(); 
			String first = firstName.getText(); 
			String last = lastName.getText(); 
			String email = emailAddress.getText(); 
			String studass = studentAss.getValue().toString(); 
			
			database.sendUserData(user, pass, first, last, email, studass); 
		}
		
		Stage stage = (Stage) send.getScene().getWindow();
		stage.close();
	}
}
