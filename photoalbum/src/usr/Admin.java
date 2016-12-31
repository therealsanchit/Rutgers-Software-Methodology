package usr;

import java.util.ArrayList;

import io.SVList;

/**
 * @author Seong Bin Park
 * main Admin object
 */
public class Admin extends Account{
	public ArrayList<User> users;

	public Admin(String name){
		super(name);
		pathToData = SVList.ADMINPATH + "/" + name;
	}

	public void setListUsers(ArrayList<User> u){
		users = u;
	}
	public String toString(){
		return name;
	}
}
