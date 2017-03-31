package GUI;

import java.util.logging.Level;
import java.util.logging.Logger;

import Software.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AskForHelp extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	JDBC database = new JDBC(); 
    	database.connect();
    	if (database.userExists()){
            Application.launch(AskForHelp.class, (String[])null);
    	}
    }

    @Override
    public void start(Stage primaryStage) {
        try {
        	JDBC database = new JDBC(); 
        	database.connect();
        	
        	AnchorPane page; 
        	Scene scene; 
        	
           	if (!database.userExists()){
           		page= (AnchorPane) FXMLLoader.load(Main.class.getResource("Register.fxml"));
                scene = new Scene(page);
                primaryStage.setScene(scene);
                primaryStage.setTitle("BuddyBOT Registration Window");
                primaryStage.show();
           	} else{ 
	            page = (AnchorPane) FXMLLoader.load(AskForHelp.class.getResource("SelfhelpGUI.fxml"));
	            scene = new Scene(page);
	            primaryStage.setScene(scene);
	            primaryStage.setTitle("BuddyBOT Selfhelp Window");
	            primaryStage.show();
           	}

        } catch (Exception ex) {
            Logger.getLogger(AskForHelp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
