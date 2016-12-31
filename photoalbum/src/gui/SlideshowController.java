package gui;

import java.util.ArrayList;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import photo.Album;
import photo.Photo;
import photo.ViewPhoto;
import usr.Account;
import usr.User;

/**
 * @author Sanchit Sharma
 * Controller for the Slideshow UI
 */
public class SlideshowController implements Controller{
	
@FXML ImageView imageview;
@FXML Button leftbutton;
@FXML Button rightbutton;
@FXML Button closebutton;

Stage slideshowStage;	
private Photo photo;
private Image image;
private Album album;
User mainUser;

ArrayList<Photo> photos = new ArrayList<Photo>();
int x;
int y;


@Override
public void setStage(Stage Stage) {
this.slideshowStage = Stage;	
}

public void setPhoto(Photo photo){
	this.photo = photo;
}
public void setImage(Image image){
	this.image = image;
}

public void setData(Account u, Album a, int y){
	this.mainUser = (User) u;	
	this.y = y;
}


@Override
public void initalize() {
	// TODO Auto-generated method stub
	populateList();
	x = photos.indexOf(photo);
	imageview.setImage(image);
}
@Override
public void populateList() {
	// TODO Auto-generated method stub
	this.album = mainUser.albums.get(y);
	this.photos = this.album.photos;
}
@Override
public void start(Stage Stage) throws Exception {
	// TODO Auto-generated method stub
	initalize();
}

public void leftButton(){
	if(x==0){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("No more photos left");
		alert.showAndWait();
		return;
	}
	if(x>0){
		x = x-1;
		Photo p = photos.get(x);
		ViewPhoto vp = new ViewPhoto(p);
		Image i = vp.getImage();
		imageview.setImage(i);
		return;
	}
}
public void rightButton(){
	if(x == photos.size() -1){
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("No more photos left");
		alert.showAndWait();
		return;
	}
	if(x<photos.size() -1){
		x = x+1;
		Photo p = photos.get(x);
		ViewPhoto vp = new ViewPhoto(p);
		Image i = vp.getImage();
		imageview.setImage(i);
		return;
	}
}
public void closeButton() throws Exception{
	FXMLLoader loader = new FXMLLoader();
	loader.setLocation(getClass().getResource("/gui/Album.fxml"));
	AnchorPane root = (AnchorPane) loader.load();

	AlbumController albumController = loader.getController();
	albumController.setData(mainUser,album);
	albumController.setStage(slideshowStage);
	albumController.start(slideshowStage);

	Scene scene = new Scene(root, 1000, 1000);
	slideshowStage.setScene(scene);
	slideshowStage.setResizable(false);

	slideshowStage.show();
}

	
}
