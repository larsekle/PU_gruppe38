package GUI;

import java.util.logging.Level;
import java.util.logging.Logger;

import Software.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
		Application.launch(Main.class, (java.lang.String[])null);
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
        	} 
        	if (database.userExists()){
        		page = (AnchorPane) FXMLLoader.load(Main.class.getResource("BuddyGUI.fxml"));
                scene = new Scene(page);
                scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                primaryStage.setScene(scene);
                primaryStage.setTitle("BuddyBOT Execution Window");
                primaryStage.show();
        	}
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
