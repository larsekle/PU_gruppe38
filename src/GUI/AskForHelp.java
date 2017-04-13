package GUI;

import java.util.logging.Level;
import java.util.logging.Logger;

import Software.JDBC;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @author Lars E. Kleiven, Ingrid E. Hermanrud, Sigrid L. Fosen, Helena Van de Pontseele
 *
 */
public class AskForHelp extends Application implements Runnable {	
	 
    /**
     * Main used to start the GUI.
     * Checks whether it is necessary to open the registration form first.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
    	JDBC database = new JDBC(); 
    	if (database.userExists()){
        	Application.launch(AskForHelp.class, (String[])null);    		
    	} else{
        	Application.launch(RegisterMain.class, (String[])null);    		
    	}
    }

    /**
     * Start method to set up and load GUI.
     * CSS is added to FXML and shown to the user. 
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage primaryStage) {
        try {     	
        	
    		AnchorPane page = (AnchorPane) FXMLLoader.load(Main.class.getResource("SelfhelpGUI.fxml"));
    		Scene scene = new Scene(page);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("BuddyBOT Selfhelp Window");
            primaryStage.show();
    	
        } catch (Exception ex) {
            Logger.getLogger(AskForHelp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

	/** 
	 * Run method is used to run main method as Runnable object through the ExecutorService class.
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		main((String[]) null); 
	}
}
