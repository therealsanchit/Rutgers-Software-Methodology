package io;

import java.util.ArrayList;

import photo.Album;
import usr.Account;
import usr.Admin;
import usr.User;

/**
 * @author kidoc_000
 * ReadIO uses DataIO methods. Since using DataIO methods are also very mundane
 * I made a class for it.
 * ... enjoy.
 */
public class ReadIO {
	
	/* HOW TO READ DATA 
	 * set LIST = (ArrayList<original class>) readAccount(....)
	 * it will set the list, then return it
	 */
	/**
	 * @param list
	 * @param reader
	 * @return ArrayList of Albums
	 * 
	 * The <?> is either Admin or User. You can use both with this it will still give you the same thing,
	 * You must input Adminreader if you want to read a list of admin accounts,
	 * input Userreader if you want to read a list of user accounts.
	 */
	public static ArrayList<?> readAccount(ArrayList<?> list, DataIO.IODataReader reader){
		list = reader.readDir();
		return list;
	}
	
	/**
	 * @param acc
	 * @param reader
	 * @return ArrayList of Albums
	 * The <?> was because I wasn't so sure about how I needed to store, but it turns out
	 * that you can just store an array of albums...
	 * So you can replace <?> with Albums, since it's only being used to read albums.
	 */
	public static ArrayList<?> getAlbum(Account acc, DataIO.IODataReader reader){
		acc.albums = reader.readAlbum(acc);

		return acc.albums;
	}
	
	/**
	 * @param list
	 * @param reader
	 * @return
	 * read ALL the albums of ALL users/admins 
	 * not sure when to use this, but use the one above for now
	 */
	public static ArrayList<?> readAlbum(ArrayList<? extends Account> list, DataIO.IODataReader reader){
		for(Account acc: list){
			acc.albums = reader.readAlbum(acc);
		}
		return list;
	}
	
	/**
	 * @param list
	 * 
	 * Just for testing.
	 * SOPs whatever arraylist.
	 */
	public static void printData(ArrayList<?> list){
		for(Object o: list){
			System.out.println(o);
		}
	}
}
