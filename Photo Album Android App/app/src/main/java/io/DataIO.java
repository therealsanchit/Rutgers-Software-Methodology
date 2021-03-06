package io;

import android.content.Context;
import android.util.Log;

import com.example.sanchitsharma.photoalbum.R;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import photo.Album;
import usr.Account;
import usr.User;

/**
 * DataIO
 * @author Seong Bin Park
 * The main IO class, most methods are static.
 * It's meant to be used without an instance, therefore we have a private DataIO
 */
public class DataIO {
	/* Starting up, we need to import a list of users and a list of admins 
	 * Our database will all be in a text format
	 */
	
	/* private so that they don't make an instance */
	private DataIO(){};
	
	
	/**
	 * @author kidoc_000
	 *
	 * @param <R> 
	 * @param <T>
	 * Parameters describe what these functions return (as an array list)
	 * This interface is to READ from a file, and is referenced as an implementation
	 * in both AdminReader and UserReader.
	 */
	public interface IODataReader<R, T>{
		public ArrayList<R> readDir(Context c);
		public ArrayList<T> readAlbum(Context c, R r);
	}
	/**
	 * @author kidoc_000
	 * This was implemented before I thought that User and Admin can be children of 'Account'.
	 * I left it like this because UserReader and Album reader return different objects as ArrayLists
	 */
	public static class IOUserReader implements IODataReader<User, Album>{
		@Override
		public ArrayList<User> readDir(Context c) {
			File f = new File(c.getFilesDir() + SVList.USERPATH);
			if(!f.exists()){
				System.out.println("IOUserReader - User folder does not exist!");
				return null;
			}
			File[] files = f.listFiles();

			ArrayList<User> users = new ArrayList<User>();

			for(File x: files){
				String id = x.getName();
				users.add(getUser(id));
			}
			return users;
		}

		/* getUser will read a folder to generate an User account
		 * the reading album / photo files will happen ONCE the user logs in. 
		 * returns User
		 */
		
		private User getUser(String id){
			User u = new User(id);
			return u;
		}
	
		/* readAlbum will take User and read the album.data of that user
		 * This will read in the whole ArrayList of albums for that user
		 */
		@Override
		public ArrayList<Album> readAlbum(Context c, User u){
			File f = new File(c.getFilesDir() + u.pathToData);
			if(!f.exists()){
				try {
					if(!f.createNewFile()){
						Log.d("ERROR", "IOUserReader - could not create data file!");
						return null;
					}
				} catch (IOException e) {e.printStackTrace();}
			}
			return inputStream(f);
		}
	}

	/**
	 * @author Seong Bin Park
	 * @param acc
	 * @return true if save is successful
	 * 
	 * Main Saving method, accessed statically.
	 */
	public static boolean save(Context c, Account acc) {
		if(acc == null)
			return false;
		Log.d("LOG_MESSAGE", "Here");
		File f = new File(c.getFilesDir() + SVList.USERPATH);
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		Log.d("LOG_MESSAGE", "Here");
		/* we write a copy first, then overwrite previous */
		File copy = new File(c.getFilesDir() + SVList.TEMP);
		if(!copy.exists()){
			try {
				copy.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}

		
		boolean successWrite = outputStream(f, copy, acc);
		/* rename the new successful copy to the real file name, then delete the old file */
		if(successWrite){
			f.delete();
			File newfile = new File(c.getFilesDir() + SVList.USERPATH);
			copy.renameTo(newfile);
			return true;
		}
		return false;
	}		
	
	/**
	 * @author Seong bin Park
	 * @param f
	 * @return ArrayList of Albums that it just read from a file
	 * 
	 * method containing how to stream info from streaming data from a file,
	 * then reading as an object
	 */
	private static ArrayList<Album> inputStream(File f){
		ArrayList<Album> albums = new ArrayList<Album>();
		FileInputStream f_in = null;
		ObjectInputStream data_in = null;
		try {
			if(f.length() != 0){
				f_in = new FileInputStream(f.getAbsolutePath());
				data_in = new ObjectInputStream(f_in);
				try {
					albums = (ArrayList<Album>) data_in.readObject();
				} catch (ClassNotFoundException e1) {e1.printStackTrace();
				} catch (IOException e1) {e1.printStackTrace();
				} finally { 
					try {
						f_in.close();
						data_in.close();
					} catch (IOException e1) {e1.printStackTrace();}
				}
			}
		} catch (EOFException e){
		} catch (IOException e) {e.printStackTrace();return null;
		}
		
		return albums;
	}
	
	/**
	 * @author Seong Bin Park
	 * @param f
	 * @param copy
	 * @param acc
	 * @return true if success
	 * method containing how to stream info from streaming data to a file,
	 */
	private static boolean outputStream(File f, File copy, Account acc){
		FileOutputStream f_out = null;
		ObjectOutputStream data_out = null;
		boolean successWrite = false;
		try{
			f_out = new FileOutputStream(copy.getAbsolutePath());
			data_out = new ObjectOutputStream(f_out);
			data_out.writeObject(acc.albums);
			successWrite = true;
		} catch (IOException e) {e.printStackTrace();return false;
		} finally {
			try {
				f_out.close();
				data_out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return successWrite;
	}
}
