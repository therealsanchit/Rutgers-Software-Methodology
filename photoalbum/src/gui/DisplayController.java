package gui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import photo.Album;
import photo.Photo;
import photo.ViewPhoto;
import usr.Account;
import usr.User;
import gui.AlbumController;

/**
 * @author Sanchit Sharma
 * Controller for the Display UI
 */
public class DisplayController {
	
	@FXML ImageView imageview;
	@FXML Label captionlabel;
	@FXML Label datelabel;
	@FXML Label taglabel;
	@FXML Button closebutton;
	
	User mainUser;
	Stage displayStage;
	private Photo photo;
	private Image image;
	private Album album;
	

	public void setStage(Stage stage){
		this.displayStage = stage;
	}
	public void setPhoto(Photo photo){
		this.photo = photo;
	}
	public void setImage(Image image){
		this.image = image;
	}
	
	public void setData(Account u, Album a){
		this.mainUser = (User) u;	
		this.album = a;	
	}
	
	private void initalize(){
		imageview.setImage(image);
		captionlabel.setText(photo.caption);
		datelabel.setText(photo.getDateOfCaptured());
		taglabel.setText(photo.getTags());
	}
	
	public void start(Stage Stage) throws Exception {
		initalize();
	}
	
	public void closeButton() throws Exception{
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/Album.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		AlbumController albumController = loader.getController();
		albumController.setData(mainUser,album);
		albumController.setStage(displayStage);
		albumController.start(displayStage);

		Scene scene = new Scene(root, 1000, 1000);
		displayStage.setScene(scene);
		displayStage.setResizable(false);

		displayStage.show();
	}
	
}
