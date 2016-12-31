package gui;

import java.util.ArrayList;

import io.DataIO;
import io.ReadIO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import photo.Album;
import usr.Account;
import usr.Admin;
import usr.User;

/**
 * @author Sanchit Sharma, Seong Bin Park
 * Controller for the Login UI - main starting page
 */
public class LoginController implements Controller {

	@FXML
	TextField name;
	@FXML
	Button enter;
	
	String result;
	
	/* READING FROM DATA ONLY EXISTS FROM LOGIN CONTROLLER */
	static DataIO.IOUserReader userReader = new DataIO.IOUserReader();
	static DataIO.IOAdminReader adminReader = new DataIO.IOAdminReader();
	
	Account loggedIn;
	ArrayList<User> users = new ArrayList<User>();
	ArrayList<String> userNames = new ArrayList<String>();
	ArrayList<Admin> admins = new ArrayList<Admin>();
	ArrayList<String> adminNames = new ArrayList<String>();
	Stage LoginStage;
	private boolean userlog = false;
	private boolean adminlog = false;

	public LoginController() {
		initalize();
	}
	
	@Override
	public void initalize() {
		populateList();
	}

	@Override
	public void populateList() {
		users = (ArrayList<User>)ReadIO.readAccount(users, userReader);
		admins = (ArrayList<Admin>) ReadIO.readAccount(admins, adminReader);
		for (int i = 0; i < users.size(); i++) {
			userNames.add(users.get(i).getName());
		}
		for (int i = 0; i < admins.size(); i++) {
			admins.get(i).setListUsers(users);
			adminNames.add(admins.get(i).getName());
		}
	}

	@Override
	public void setStage(Stage Stage) {
		this.LoginStage = Stage;
	}

	@Override
	public void start(Stage Stage) throws Exception {

		if (userlog) {
			
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/User.fxml"));
			AnchorPane root = (AnchorPane) loader.load();

			UserController userController = loader.getController();
			userController.setData((ArrayList<Album>)ReadIO.getAlbum(loggedIn, userReader));
			userController.setStage(Stage);
			userController.setUser(getUser(result));

			userController.start(Stage);

			Scene scene = new Scene(root, 1000, 600);
			Stage.setScene(scene);
			Stage.setResizable(false);

			Stage.show();
		}

		if (adminlog) {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/gui/Admin.fxml"));
			AnchorPane root = (AnchorPane) loader.load();

			AdminController adminController = loader.getController();
			adminController.setData(loggedIn, admins, users);
			adminController.setStage(Stage);
			adminController.start(Stage);

			Scene scene = new Scene(root, 1000, 1000);
			Stage.setScene(scene);
			Stage.setResizable(false);

			Stage.show();
		}

	}

	public void enterButton() throws Exception {
		 result = name.getText();
		if (userNames.contains(result)) {
			userlog = true;
			loggedIn = getUser(result);
			start(LoginStage);
		}

		if (adminNames.contains(result)) {
			adminlog = true;
			loggedIn = getAdmin(result);
			start(LoginStage);
		}
	}

	private User getUser(String user){
		int index = userNames.indexOf(user);
		User returnuser = users.get(index);
		return returnuser;
	}
	private Admin getAdmin(String admin){
		int index = adminNames.indexOf(admin);
		Admin returnadmin = admins.get(index);
		return returnadmin;
	}
}
