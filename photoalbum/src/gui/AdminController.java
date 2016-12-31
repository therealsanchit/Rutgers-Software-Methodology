package gui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import io.DataIO;
import io.SVList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import usr.Account;
import usr.Admin;
import usr.User;

/**
 * @author Sanchit Sharma, Seong Bin Park
 * controller for the Admin UI
 */
public class AdminController implements Controller{

	@FXML
	ListView<String> userlist;
	@FXML
	Button create;
	@FXML
	Button delete;
	@FXML
	Button logout;

	ArrayList<User> users = new ArrayList<User>();
	ObservableList<String> userNames = FXCollections.observableArrayList();
	ArrayList<Admin> admins = new ArrayList<Admin>();
	ArrayList<String> adminNames = new ArrayList<String>();
	Admin admin;
	
	Stage adminStage;
	
	@Override
	public void initalize() {
		populateList();	
	}

	@Override
	public void populateList() {
		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			String name = user.getName();
			userNames.add(name);
		}
		for (int i = 0; i < admins.size(); i++) {
			Admin admin = admins.get(i);
			String name = admin.getName();
			adminNames.add(name);
		}
		userlist.setItems(userNames);
	}

	@Override
	public void start(Stage Stage) throws Exception {
		initalize();
	}
	
	public void setData(Account a, ArrayList<Admin> admins, ArrayList<User> users){
		this.admin = (Admin) a;
		this.admins = admins;
		this.users = users;
	}

	@Override
	public void setStage(Stage Stage) {
		this.adminStage = Stage;		
	}
	
	
	public void deleteButton() {
		if(!checkSelected())
			return;

		String userS = userlist.getSelectionModel().getSelectedItem();

		User user = users.get(userNames.indexOf(userS));
		
		File f = new File("user/" + user.getName());
		if(f.exists())
			deleteDir(f);
		
		users.remove(user);
		userNames.clear();
		populateList();
	}

	public void createButton() {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Create User");
		dialog.setContentText("Enter name: ");
		dialog.setHeaderText("Details");

		Optional<String> result = dialog.showAndWait();

		if (result.isPresent()) {
			String newuser = result.get();

			if (newuser.isEmpty()) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Cannot Create");
				alert.setContentText("Please enter a valid name");
				alert.showAndWait();
				return;
			}

			if (checkNameExists(users, newuser) || checkNameExists(admins, newuser)) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setHeaderText("Cannot Create");
				alert.setTitle("Error");
				alert.setContentText("Name already exsists");
				alert.showAndWait();
				return;

			} else {
				File f = new File("user/" + newuser);
				if(!f.exists())
					f.mkdir();
				
				User user = new User(newuser);
				users.add(user);
				userNames.add(newuser);
				userlist.setItems(userNames);
				
				if(DataIO.save(user)){
					Alert alertS = SVList.success("User Saved");
					alertS.show();
				};
			}
		}
		return;
	}

	public void logoutButton() throws Exception{
		FXMLLoader loader = new FXMLLoader();   
		loader.setLocation(getClass().getResource("/app/Start.fxml"));
		AnchorPane root = (AnchorPane)loader.load();
		
		LoginController loginController = loader.getController();
		loginController.setStage(adminStage);
		loginController.start(adminStage);

		Scene scene = new Scene(root, 500, 250);
		adminStage.setScene(scene);
		adminStage.setResizable(false);  

		adminStage.show(); 
	
	}
	
	public boolean checkSelected(){
		if(userlist.getSelectionModel().getSelectedItem() == null){
			Alert a = SVList.failure("You didn't select anything!");
			a.show();
			return false;
		}
		return true;
	}
	
	public <R> boolean checkNameExists(ArrayList<R> list, String name){
		for(R s: list){
			Account acc = (Account)s;
			if(acc.name.equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
	/**
	 * @param file
	 * So when we delete a user, we need to delete the folder that holds the user's name.
	 * in order to delete a folder, we need to recursively delete what's inside.
	 */
	public void deleteDir(File file) {
	    File[] contents = file.listFiles();
	    if (contents != null) {
	        for (File f : contents) {
	            deleteDir(f);
	        }
	    }
	    file.delete();
	}
}
