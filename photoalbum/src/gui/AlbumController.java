package gui;

import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import io.DataIO;
import io.ReadIO;
import io.SVList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Callback;
import photo.Album;
import photo.Photo;
import photo.Tag;
import photo.ViewAlbum;
import photo.ViewPhoto;
import usr.Account;
import usr.User;

/**
 * @author Sanchit Sharma, Seong Bin Park
 * Main Controller for Albums
 * It holds functions for all the buttons, holds user data / single album data
 */
public class AlbumController implements Controller {

	@FXML
	Button addphoto;
	@FXML
	Button deletephoto;
	@FXML
	Button movephoto;
	@FXML
	Button displayphoto;
	@FXML
	Button slideshow;
	@FXML
	Button editcaption;
	@FXML
	Button addtag;
	@FXML
	Button removetag;
	@FXML
	Button close;
	@FXML
	ScrollPane scroll;
	@FXML
	TilePane tile;
	@FXML
	TableView<ViewPhoto> phototable;
	@FXML
	TableColumn<ViewPhoto, ImageView> photocol;
	@FXML
	TableColumn<ViewPhoto, String> captioncol;
	@FXML
	Label albumlabel;

	Stage albumStage;

	private Album album;
	private Account mainUser;
	ArrayList<Photo> photos;
	ArrayList<String> photoNames = new ArrayList<String>();
	ArrayList<ViewPhoto> viewPhotos = new ArrayList<ViewPhoto>();
	ObservableList<ViewPhoto> info;
	int albumIndex;
	boolean isTemp;
	
	/**
	 * @param u
	 * @param temp
	 * This is used to initalize this album UI with the results of the search function.
	 * Since the album is not connected as a list under a user yet, we pass the album itself.
	 */
	public void setTempData(User u, Album temp){
		this.mainUser = u;
		this.album = temp;
		this.photos = this.album.photos;
		for (Photo p : photos) {
			viewPhotos.add(new ViewPhoto(p));
			photoNames.add(p.name);
		}
		albumlabel.setText(album.name);
		isTemp = true;
	}
	/**
	 * @param u
	 * @param a
	 * This is used to initalize this album UI with the selection from previous UI.
	 */
	public void setData(User u, Album a) {
		this.mainUser = u;
		/*
		 * we need to set this.album to the u's connected Album, not the static
		 * album passed as argument
		 */
		albumIndex = -1;
		for (int x = 0; x < u.albums.size(); x++) {
			if (u.albums.get(x).equals(a)) {
				albumIndex = x;
				break;
			}
		}
		if (albumIndex < 0) {
			System.out.println("SOMETHING IS WRONG - DATA NOT LOADED");
			return;
		}

		this.album = u.albums.get(albumIndex);
		this.photos = this.album.photos;
		for (Photo p : photos) {
			viewPhotos.add(new ViewPhoto(p));
			photoNames.add(p.name);
		}
		albumlabel.setText(album.name);
		isTemp = false;
	}

	@Override
	public void initalize() {
		populateList();
	}

	@Override
	public void populateList() {
		Collections.sort(viewPhotos);
		info = FXCollections.observableArrayList(viewPhotos);
		phototable.setItems(info);
	}

	/**
	 * We need to call this instead of populateList in order to refresh.
	 * This is important because of the two lines of code at the end.
	 */
	private void refreshList() {
		Collections.sort(viewPhotos);
		info = FXCollections.observableList(viewPhotos);
		phototable.setItems(info);
		phototable.getColumns().get(0).setVisible(false);
		phototable.getColumns().get(0).setVisible(true);
	}

	@Override
	public void start(Stage Stage) throws Exception {
		initalize();
	}

	@Override
	public void setStage(Stage Stage) {
		this.albumStage = Stage;
	}

	/**
	 * @throws Exception
	 * main slideshow method
	 * initalizes the slideshowController
	 */
	public void slideshowPhoto() throws Exception {
		if (!checkSelected())
			return;

		Alert alert = SVList.confirmation();
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			if (info.size() == 0) {
				Alert a1 = SVList.failure("There was nothing to display.");
				a1.show();
				return;
			}

			ViewPhoto vp = phototable.getSelectionModel().getSelectedItem();
			Photo p = vp.p;
			Image i = vp.getImage();
	
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/Slideshow.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
	
			SlideshowController slideshowController = loader.getController();
			slideshowController.setData(mainUser, album,albumIndex);
			slideshowController.setPhoto(p);
			slideshowController.setImage(i);
			slideshowController.setStage(albumStage);
			slideshowController.start(albumStage);
	
			Scene scene = new Scene(root, 1000, 1000);
			albumStage.setScene(scene);
			albumStage.setResizable(false);
	
			albumStage.show();
		}
	}


	/**
	 * @throws Exception
	 * Initalizes the displayController
	 */
	public void displayPhoto() throws Exception {
		if (!checkSelected())
			return;

		Alert alert = SVList.confirmation();
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			if (info.size() == 0) {
				Alert a1 = SVList.failure("There was nothing to display.");
				a1.show();
				return;
			}
			
	
			ViewPhoto vp = phototable.getSelectionModel().getSelectedItem();
			Photo p = vp.p;
			Image i = vp.getImage();
	
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/Display.fxml"));
			AnchorPane root = (AnchorPane) loader.load();
	
			DisplayController displayController = loader.getController();
			displayController.setData(mainUser, album);
			displayController.setPhoto(p);
			displayController.setImage(i);
			displayController.setStage(albumStage);
			displayController.start(albumStage);
	
			Scene scene = new Scene(root, 1000, 1000);
			albumStage.setScene(scene);
			albumStage.setResizable(false);
	
			albumStage.show();
		}
	}

	/**
	 * Adding a photo
	 */
	public void addPhoto() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Add Photo");
		dialog.setHeaderText("Enter Path to Photo");
		dialog.setContentText("Path: ");

		Optional<String> result = dialog.showAndWait();

		if (result.isPresent()) {
			if (Photo.findValidPath(result.get())) {
				String path = result.get();

				if (checkPhotoPathExists(photos, path)) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("Path already exists");
					alert.showAndWait();
					return;
				}

				TextInputDialog name = new TextInputDialog();
				name.setTitle("Add Photo");
				name.setHeaderText("Enter Name");
				name.setContentText("Name: ");

				Optional<String> result1 = name.showAndWait();

				if (result1.isPresent()) {

					if (result1.get().isEmpty()) {
						SVList.throwEmptyError();
						return;
					}

					if (checkPhotoNameExists(photos, result1.get())) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("Photo Name already exists");
						alert.showAndWait();
						return;
					}

					String photoname = result1.get();

					TextInputDialog caption = new TextInputDialog();
					caption.setTitle("Add Photo");
					caption.setHeaderText("Enter Caption");
					caption.setContentText("Caption: ");

					Optional<String> result2 = caption.showAndWait();

					String photocaption = "";
					if (result2.isPresent())
						photocaption = result2.get();
					else
						photocaption = "Empty";

					/* CREATE PHOTO HERE */
					Photo p = new Photo(photoname, photocaption, path);
					album.addToPhotos(p);
					viewPhotos.add(new ViewPhoto(p));
					refreshPhotoNames();
					ReadIO.printData(album.photos);
					if (DataIO.save(mainUser)) {
						Alert alertS = SVList.success("Save Success");
						alertS.show();
					}
					;
				}
			} else {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Not a valid path");
				alert.showAndWait();
			}

		}
		refreshList();
	}

	/**
	 * Removing a photo from the list
	 */
	public void removePhoto() {
		if (!checkSelected())
			return;

		Alert alert = SVList.confirmation();
		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			if (info.size() == 0) {
				Alert a1 = SVList.failure("There was nothing to delete.");
				a1.show();
				return;
			}
			ViewPhoto vp = phototable.getSelectionModel().getSelectedItem();
			Photo p = vp.p;

			viewPhotos.remove(vp);

			album.deleteFromPhotos(p);
			vp = null;
			p = null;

			refreshPhotoNames();
			if (DataIO.save(mainUser)) {
				Alert alertS = SVList.success("Delete Success");
				alertS.show();
			}
			;
			refreshList();
		}
	}

	/**
	 * Moving a photo from one album to another
	 */
	public void movePhoto() {
		if (!checkSelected())
			return;

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Move Photo");
		dialog.setHeaderText("Enter Destination:");
		dialog.setContentText("Album Name: ");

		Optional<String> result = dialog.showAndWait();

		if (result.isPresent()) {
			if (result.get().isEmpty()) {
				SVList.throwEmptyError();
				return;
			}

			String destination = result.get();
			Album to = null;
			// check for existing album name
			if (destination.equals(album.name) || (to = getAlbumFromAlbumName(destination)) == null) {
				Alert a = SVList.failure("Error in Album Name!");
				a.show();
				return;
			}
			// check if existing album to has a path that is already
			// destination's path
			ViewPhoto vp = phototable.getSelectionModel().getSelectedItem();

			Photo p = vp.p;

			if (checkPhotoPathExists(to.photos, p.path) || checkPhotoNameExists(to.photos, p.name)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Photo already exists");
				alert.showAndWait();
				return;
			}

			// final confirmation
			Alert alert = SVList.confirmation();
			Optional<ButtonType> result2 = alert.showAndWait();
			if (result2.get() == ButtonType.OK) {
				/* action here */
				/* delete from here first */
				viewPhotos.remove(vp);
				album.deleteFromPhotos(p);

				/* add to Album to (already checked if null) */
				to.addToPhotos(p);

				refreshPhotoNames();

				if (DataIO.save(mainUser)) {
					Alert alertS = SVList.success("Move Success");
					alertS.show();
				}
				;
				refreshList();
			}
		}
	}

	/**
	 * editing a field caption inside an album
	 */
	public void editCaption() {
		if (!checkSelected())
			return;

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Edit Caption");
		dialog.setHeaderText("Enter New Caption:");
		dialog.setContentText("Caption: ");

		Optional<String> result = dialog.showAndWait();

		if (result.isPresent()) {
			String newCaption = result.get();
			ViewPhoto vp = phototable.getSelectionModel().getSelectedItem();
			vp.editCaption(newCaption);

			if (DataIO.save(mainUser)) {
				Alert alertS = SVList.success("Edit Success");
				alertS.show();
			}
			;
			refreshList();
		}
	}

	/**
	 * Adding a tag to a photo
	 */
	public void addTag() {
		if (!checkSelected())
			return;

		ViewPhoto vp = phototable.getSelectionModel().getSelectedItem();

		/* ask for tag type */
		List<String> types = Arrays.asList(SVList.TAG_TYPES);
		Dialog dialog = new ChoiceDialog(types.get(0), types);
		dialog.setTitle("Add Tag");
		dialog.setHeaderText("Select Tag Type:");
		dialog.setContentText("Type: ");

		Optional<String> result = dialog.showAndWait();
		String selected = "";
		if (result.isPresent())
			selected = result.get();
		else
			return;

		/* ask for tag value */
		TextInputDialog dialog2 = new TextInputDialog();
		dialog2.setTitle("Add Tag");
		dialog2.setHeaderText("Enter Tag Value:");
		dialog2.setContentText("Value: ");

		Optional<String> result2 = dialog2.showAndWait();

		String value = "";
		if (result2.isPresent()) {
			value = result2.get();
		}
		
		if(value.length() == 0){
			SVList.failure("Enter Valid Tag Value").show();
			return;
		}
		/* add the tag */
		Tag t = new Tag(Tag.Type.valueOf(selected.toUpperCase()), value);

		if (vp.addTags(t)) {
			if (DataIO.save(mainUser)) {
				Alert alertS = SVList.success("Tag Saved");
				alertS.show();
			}
			;
			refreshList();
		} else {
			Alert al = SVList.failure("Tag Not Saved");
			al.show();
		}
	}

	/**
	 * deleting a tag from a photo
	 */
	public void deleteTag() {
		if (!checkSelected())
			return;

		ViewPhoto vp = phototable.getSelectionModel().getSelectedItem();

		/* ask for tag type */
		List<String> types = Arrays.asList(SVList.TAG_TYPES);
		Dialog dialog = new ChoiceDialog(types.get(0), types);
		dialog.setTitle("Remove Tag");
		dialog.setHeaderText("Select Tag Type:");
		dialog.setContentText("Type: ");

		Optional<String> result = dialog.showAndWait();
		String selected = "";
		if (result.isPresent())
			selected = result.get();
		else
			return;

		/* ask for tag value */
		TextInputDialog dialog2 = new TextInputDialog();
		dialog2.setTitle("Remove Tag");
		dialog2.setHeaderText("Enter Tag Value:");
		dialog2.setContentText("Value: ");

		Optional<String> result2 = dialog2.showAndWait();

		String value = "";
		if (result2.isPresent()) {
			value = result2.get();
		} else
			return;

		/* delete the tag */

		if (vp.deleteTags(Tag.Type.valueOf(selected.toUpperCase()), value)) {
			if (DataIO.save(mainUser)) {
				Alert alertS = SVList.success("Tag Deleted");
				alertS.show();
			}
			refreshList();
		} else {
			Alert al = SVList.failure("Tag Not Deleted");
			al.show();
		}
	}
	
	/**
	 * Saving an album
	 * This is mostly for saving the search results, but even if user clicks on this
	 * with the regular albums, it will just save.
	 * 
	 * We keep the boolean isTemp to track if this UI was initalized with the temp method
	 */
	public void saveAlbum(){
		if(!isTemp){
			if (DataIO.save(mainUser)) {
				Alert alertS = SVList.success("Saved");
				alertS.show();
				refreshList();
			}else{
				Alert al = SVList.failure("Not Saved");
				al.show();
			}
			return;
		}
		
		/* ask for name of album */
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Create Album");
		dialog.setHeaderText("Enter Details");
		dialog.setContentText("Enter Name of album");

		Optional<String> result = dialog.showAndWait();

		if (result.isPresent()) {
			String newalbum = result.get();

			if (newalbum.isEmpty()) {
				SVList.throwEmptyError();
				return;
			}
			
			album.name = newalbum;
			
			for(Album a: mainUser.albums){
				if(a.equals(album)){
					SVList.sameNameError();
					return;
				}
			}

			Alert alert = SVList.confirmation();
			Optional<ButtonType> confirm = alert.showAndWait();

			if (confirm.get() == ButtonType.OK) {
				mainUser.albums.add(album);

				if (DataIO.save(mainUser)) {
					Alert alertS = SVList.success("Save Success");
					alertS.show();
				}
				refreshList();
			}
		}
	}
	
	/**
	 * @throws Exception
	 * Going back to the previous Window, which was the USER UI
	 */
	public void goBack() throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/User.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		UserController userController = loader.getController();
		userController.setData(mainUser.albums);
		userController.setStage(albumStage);
		String result = mainUser.getName();
		userController.setUser(mainUser);

		userController.start(albumStage);

		Scene scene = new Scene(root, 1000, 600);
		albumStage.setScene(scene);
		albumStage.setResizable(false);

		albumStage.show();
	}

	/**
	 * These are a bunch of helper methods that we keep on reusing.
	 * The names actually explain it all...
	 */
	private Photo findPhoto(String name) {
		for (Photo p : photos) {
			if (p.name.equalsIgnoreCase(name))
				;
			return p;
		}
		return null;
	}

	private boolean checkPhotoPathExists(ArrayList<Photo> pList, String path) {
		System.out.println(path);
		System.out.println("---");
		for (Photo p : pList) {
			System.out.println(p.path);
			if (p.path.equals(path)) {
				System.out.println("found");
				return true;
			}
		}
		return false;
	}

	private boolean checkPhotoNameExists(ArrayList<Photo> pList, String name) {
		for (Photo p : pList) {
			if (p.name.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	private Album getAlbumFromAlbumName(String destination) {
		for (Album a : mainUser.albums) {
			if (a.name.equals(destination))
				return a;
		}
		return null;
	}

	public boolean checkSelected() {
		if (phototable.getSelectionModel().getSelectedItem() == null) {
			Alert a = SVList.failure("You didn't select anything!");
			a.show();
			return false;
		}
		return true;
	}

	private void refreshPhotoNames() {
		photoNames.clear();
		for (ViewPhoto v : viewPhotos) {
			photoNames.add(v.getNameproperty());
		}
	}
}
