package GUI;

import java.util.logging.Level;
import java.util.logging.Logger;

import Software.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AskForHelp extends Application implements Runnable {
	
	public static boolean test = false;
	
	
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	Application.launch(AskForHelp.class, (String[])null);
    }

    @Override
    public void start(Stage primaryStage) {
        try {      	
        	
        	JDBC database = new JDBC(); 
        	database.connect();
        	
        	AnchorPane page;
        	Scene scene;
        	
        	if (!database.userExists() || test){
        		 page= (AnchorPane) FXMLLoader.load(Main.class.getResource("Register.fxml"));
                 scene = new Scene(page);
                 scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                 primaryStage.setScene(scene);
                 primaryStage.setTitle("BuddyBOT Registration Window");
                 if (!test) primaryStage.show();
                                        
        	} 
        	if (database.userExists() || test){
        		page = (AnchorPane) FXMLLoader.load(Main.class.getResource("SelfhelpGUI.fxml"));
                scene = new Scene(page);
                scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
                primaryStage.setScene(scene);
                primaryStage.setTitle("BuddyBOT Selfhelp Window");
                primaryStage.show();
                if (test) primaryStage.close();
        	} 

        } catch (Exception ex) {
            Logger.getLogger(AskForHelp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

	@Override
	public void run() {
		main((String[]) null); 
	}
}
