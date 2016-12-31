package io;

import java.util.ArrayList;

import photo.Album;
import usr.Admin;
import usr.User;

/**
 * @author kidoc_000
 * Just for test
 */
public class TestIO {
	public static ArrayList<User> users;
	public static ArrayList<Admin> admins;
	
	public static void main(String[] args){
		//readData(users, DataIO.IOUserReader.class);
		users = (ArrayList<User>)ReadIO.readAccount(users, new DataIO.IOUserReader());
		users = (ArrayList<User>)ReadIO.readAlbum(users, new DataIO.IOUserReader());
		admins = (ArrayList<Admin>) ReadIO.readAccount(admins, new DataIO.IOAdminReader());
		ReadIO.printData(admins);
		ReadIO.printData(users);
	}
}
