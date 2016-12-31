package app;

import gui.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * @author Sanchit Sharma
 * Main METHOD
 */
public class PhotoAlbum extends Application{
	
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(getClass().getResource("Start.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		
		LoginController loginController = loader.getController();
		loginController.setStage(primaryStage);

		Scene scene = new Scene(root, 500, 250);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);  

		primaryStage.show(); 
	}

	public static void main(String[] args) {

		// TODO Auto-generated method stub
		launch(args);
		
	}
}
