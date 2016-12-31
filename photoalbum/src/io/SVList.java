package io;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * @author kidoc_000
 * SVList will contain all of the static final information
 */
public class SVList {
	public final static String ADMINPATH = "admin";
	
	public final static String USERPATH = "user";
	public final static String ALBUMS = "/albums"; 
	public final static String TEMP = "/temp";
	
	public final static DateFormat dateformat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
	
	public final static String[] TAG_TYPES = {"PERSON", "LOCATION", "COLOR", "MOOD"};
	
	public final static Alert confirmation() {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirm");
		alert.setContentText("");
		alert.setHeaderText("Proceed with this action?");
		return alert;
	}
	public final static Alert success(String msg) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Success!");
		alert.setContentText("Action Success");
		alert.setHeaderText(msg);
		return alert;
	}
	public final static Alert failure(String msg) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setContentText("Action Failure");
		alert.setHeaderText(msg);
		return alert;
	}

	public final static void sameNameError() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ErroR");
		alert.setContentText("Album name already exsists");
		alert.setHeaderText("Could not create");
		alert.showAndWait();
	}

	public final static void throwEmptyError() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("ErroR");
		alert.setContentText("Please Enter a valid name");
		alert.setHeaderText("Could not create");
		alert.showAndWait();
	}
}
