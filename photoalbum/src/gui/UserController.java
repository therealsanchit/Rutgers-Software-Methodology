package gui;

import java.sql.Date;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import photo.Album;
import photo.Photo;
import photo.Tag;
import photo.ViewAlbum;
import usr.Account;
import usr.User;

/**
 * @author Sanchit Sharma, Seong Bin Park
 * Controller for the User UI
 */
public class UserController implements Controller {

	@FXML
	TableView<ViewAlbum> albumtable;
	@FXML
	Button delete;
	@FXML
	Button create;
	@FXML
	Button open;
	@FXML
	Button rename;
	@FXML
	Button search;
	@FXML
	Button logout;
	@FXML
	TableColumn<ViewAlbum, String> namecol;
	@FXML
	TableColumn<ViewAlbum, String> oldestcol;
	@FXML
	TableColumn<ViewAlbum, Integer> integercol;
	@FXML
	TableColumn<ViewAlbum, String> rangecol;

	Account mainUser;
	Stage userStage;
	ArrayList<Album> albums = new ArrayList<Album>();
	ArrayList<ViewAlbum> viewAlbums = new ArrayList<ViewAlbum>();
	ArrayList<String> albumNames = new ArrayList<String>();

	ObservableList<ViewAlbum> info;

	public void setUser(Account acc) {
		this.mainUser = acc;
	}

	/* USE TRANSFERDATA TO SET ARRAYLISTS */
	public void setData(ArrayList<Album> albums) {
		this.albums = albums;
		for (Album a : albums) {
			viewAlbums.add(new ViewAlbum(a));
			albumNames.add(a.name);
		}
	}

	@Override
	public void initalize() {
		populateList();
	}

	@Override
	public void populateList() {
		Collections.sort(viewAlbums);
		info = FXCollections.observableList(viewAlbums);
		albumtable.setItems(info);
	}

	private void refreshList() {
		Collections.sort(viewAlbums);
		info = FXCollections.observableList(viewAlbums);
		albumtable.setItems(info);
		albumtable.getColumns().get(0).setVisible(false);
		albumtable.getColumns().get(0).setVisible(true);
	}

	@Override
	public void start(Stage Stage) throws Exception {
		initalize();
	}

	@Override
	public void setStage(Stage Stage) {
		this.userStage = Stage;
	}

	public void logout() throws Exception {

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/app/Start.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		LoginController loginController = loader.getController();
		loginController.setStage(userStage);
		loginController.start(userStage);

		Scene scene = new Scene(root, 500, 250);
		userStage.setScene(scene);
		userStage.setResizable(false);

		userStage.show();
	}

	/**
	 * @throws Exception
	 * These search methods call the real search method on Account
	 */
	public void search() throws Exception {
		String[] choicesArr = { "By Date", "By Tag" };
		List<String> choices = Arrays.asList(choicesArr);
		Dialog d1 = new ChoiceDialog(choices.get(0), choices);
		d1.setTitle("Search");
		d1.setHeaderText("Select Method of Search:");
		d1.setContentText("By: ");
		Optional<String> result = d1.showAndWait();
		String selectedchoice = "";
		if (result.isPresent())
			selectedchoice = result.get();
		else
			return;

		ArrayList<Photo> resultPhotos = null;
		if (selectedchoice.equals("By Date"))
			resultPhotos = searchByDate();
		else
			resultPhotos = searchByTag();
		
		if(resultPhotos == null){
			SVList.failure("We need proper search attributes").show();
			return;
		}
		//ReadIO.printData(resultPhotos);
		Album temp = new Album(resultPhotos);
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/Album.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		AlbumController albumController = loader.getController();
		albumController.setTempData((User) mainUser, temp);
		albumController.setStage(userStage);
		albumController.start(userStage);

		Scene scene = new Scene(root, 700, 510);
		userStage.setScene(scene);
		userStage.setResizable(false);

		userStage.show();
	}

	public ArrayList<Photo> searchByDate() {
		Dialog<ArrayList<Photo>> dialog = new Dialog<>();
		dialog.setTitle("Search");
		dialog.setHeaderText("Fill in Date Range");
		Label label1 = new Label("Beginning:");
		Label label2 = new Label("End:");

		DatePicker date1 = new DatePicker();
		DatePicker date2 = new DatePicker();

		GridPane grid = new GridPane();
		grid.add(label1, 1, 1);
		grid.add(date1, 2, 1);
		grid.add(label2, 1, 2);
		grid.add(date2, 2, 2);
		dialog.getDialogPane().setContent(grid);

		ButtonType buttonSearch = new ButtonType("Search", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonSearch);

		dialog.setResultConverter(new Callback<ButtonType, ArrayList<Photo>>() {
			@Override
			public ArrayList<Photo> call(ButtonType b) {
				if (b == buttonSearch) {
					if(date1.getValue() == null || date2.getValue() == null)
						return null;
					Date s = Date.valueOf(date1.getValue());
					Date e = Date.valueOf(date2.getValue());
					if(s.compareTo(e) > 0)
						return null;
					return mainUser.searchByDateRange(s, e);
				}
				return null;
			}
		});

		Optional<ArrayList<Photo>> result = dialog.showAndWait();
		if (result.isPresent()) {
			return result.get();
		}
		return null;
	}

	public ArrayList<Photo> searchByTag() {
		Dialog<ArrayList<Photo>> dialog = new Dialog<>();
		dialog.setTitle("Search");
		dialog.setHeaderText("Fill in Tag Info");
		Label label1 = new Label("Type");
		Label label2 = new Label("Value:");

		List<String> types = Arrays.asList(SVList.TAG_TYPES);
		ChoiceBox cb = new ChoiceBox();
		cb.setItems(FXCollections.observableArrayList(types));
		TextField tf = new TextField();

		GridPane grid = new GridPane();
		grid.add(label1, 1, 1);
		grid.add(cb, 2, 1);
		grid.add(label2, 1, 2);
		grid.add(tf, 2, 2);
		dialog.getDialogPane().setContent(grid);

		ButtonType buttonSearch = new ButtonType("Search", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().add(buttonSearch);

		dialog.setResultConverter(new Callback<ButtonType, ArrayList<Photo>>() {
			@Override
			public ArrayList<Photo> call(ButtonType b) {
				if (b == buttonSearch) {
					if(cb.getValue() == null || tf.getText().length() == 0)
						return null;
					return mainUser.searchByTagPair(Tag.Type.valueOf(((String) cb.getValue()).toUpperCase()),
							tf.getText());
				}
				return null;
			}
		});

		Optional<ArrayList<Photo>> result = dialog.showAndWait();
		if (result.isPresent()) {
			return result.get();
		}
		return null;
	}

	public void openAlbum() throws Exception {
		if (!checkSelected())
			return;

		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("/gui/Album.fxml"));
		AnchorPane root = (AnchorPane) loader.load();

		AlbumController albumController = loader.getController();
		albumController.setData((User) mainUser, albumtable.getSelectionModel().getSelectedItem().a);
		albumController.setStage(userStage);
		albumController.start(userStage);

		Scene scene = new Scene(root, 700, 510);
		userStage.setScene(scene);
		userStage.setResizable(false);

		userStage.show();
	}

	public void renameButton() {
		if (!checkSelected())
			return;

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Rename Album");
		dialog.setHeaderText("Enter Details");
		dialog.setContentText("Enter new name");

		Optional<String> result = dialog.showAndWait();

		if (result.isPresent()) {

			String rename = result.get();

			if (rename.isEmpty()) {
				SVList.throwEmptyError();
				return;
			}
			if (albumNames.contains(rename)) {
				SVList.sameNameError();
				return;
			}

			Alert alert = SVList.confirmation();
			Optional<ButtonType> confirm = alert.showAndWait();

			if (confirm.get() == ButtonType.OK) {
				ViewAlbum va = albumtable.getSelectionModel().getSelectedItem();
				va.a.name = rename;
				va.setNameproperty(rename);
				refreshAlbumNames();

				if (DataIO.save(mainUser)) {
					Alert alertS = SVList.success("Save Success");
					alertS.show();
				}
				refreshList();
			}
		}
	}

	public void createButton() {
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

			if (albumNames.contains(newalbum)) {
				SVList.sameNameError();
				return;
			}

			Alert alert = SVList.confirmation();
			Optional<ButtonType> confirm = alert.showAndWait();

			if (confirm.get() == ButtonType.OK) {
				Album a = new Album(newalbum);
				albums.add(a);
				viewAlbums.add(new ViewAlbum(a));
				refreshAlbumNames();
				// need to add to albumtable?
				if (DataIO.save(mainUser)) {
					Alert alertS = SVList.success("Save Success");
					alertS.show();
				}
				refreshList();
			}
		}
	}

	public void deleteButton() {
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
			ViewAlbum va = albumtable.getSelectionModel().getSelectedItem();
			Album a = va.a;

			albums.remove(a);
			viewAlbums.remove(va);
			refreshAlbumNames();
			if (DataIO.save(mainUser)) {
				Alert alertS = SVList.success("Delete Success");
				alertS.show();
			}
			;
			refreshList();
		}
	}

	public boolean checkSelected() {
		if (albumtable.getSelectionModel().getSelectedItem() == null) {
			Alert a = SVList.failure("You didn't select anything!");
			a.show();
			return false;
		}
		return true;
	}

	private void refreshAlbumNames() {
		albumNames.clear();
		for (ViewAlbum a : viewAlbums) {
			albumNames.add(a.getNameproperty());
		}
	}
}
