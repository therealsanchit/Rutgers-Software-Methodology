package photo;

import java.io.BufferedReader;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.SVList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import usr.User;

/**
 * @author Seong Bin Park
 * Main Album object class
 */
public class Album implements Serializable, Comparable<Album>{
	private static final long serialVersionUID = -5903254468625127378L;
	
	private String user;
	public String name;
	public String photoTXT;
	public int numOfPhotos;
	public Date lastPhoto;
	public Date firstPhoto;
	public ArrayList<Photo> photos;

	public Album(ArrayList<Photo> p){
		photos = p;
		numOfPhotos = p.size();
		refreshDateValues();
	}
	public Album(String name){
		this.name = name;
		this.photoTXT = "";
		this.numOfPhotos = 0;
		this.lastPhoto = null;
		this.firstPhoto = null;
		photos = new ArrayList<Photo>();
	}
	
	/**
	 * @param p
	 * add to Photos and delete from Photos are used to add / delete from array list of photos
	 * you should NEVER do this by hand, always call this if you want to add / delete
	 * used to mimic atomicity... kinda
	 */
	public void addToPhotos(Photo p){
		photos.add(p);
		numOfPhotos++;
		changeDateValues(p.captured);
	}
	public void deleteFromPhotos(Photo p){
		for(int x=0; x<photos.size(); x++){
			if(photos.get(x).equals(p)){
				photos.remove(x);
				numOfPhotos--;
				refreshDateValues();
				break;
			}
		}
	}
	public List<Photo> getPhotos(){
		return photos;
	}
	
	public String getUser(){return user;}
		

	/**
	 * @param photoCaptured
	 * Make sure you call this method whenever a photo has been modified / added
	 */
	public void changeDateValues(Date photoCaptured){
		// if there is no last photo or photocaptured is after last photo
		if(lastPhoto == null || photoCaptured.compareTo(lastPhoto) > 0)
			lastPhoto = photoCaptured;
		// if there is no firstphoto or this photocaptured is before first photo
		if(firstPhoto == null || photoCaptured.compareTo(firstPhoto) < 0)
			firstPhoto = photoCaptured;
	}
	
	/**
	 * Make sure you call this method whenever a photo has been deleted 
	 */
	public void refreshDateValues(){
		if(photos.size() == 0){
			lastPhoto = null;
			firstPhoto = null;
			return;
		}
		Date first = photos.get(0).captured;
		Date last = first;
		for(Photo p: photos){
			if(p.captured.compareTo(last) > 0)
				last = p.captured;
			if(p.captured.compareTo(first) < 0)
				first = p.captured;
		}
		lastPhoto = last;
		firstPhoto = first;
	}
	
	@Override
	public int compareTo(Album o) {
		return this.name.compareTo(o.name);
	}
	
	public String toString(){
		String value = "";
		if(name != null)
			value += name + "; ";
		
		value += numOfPhotos + "; ";
		
		if(lastPhoto != null)
			value += SVList.dateformat.format(lastPhoto) + "; ";
			
		if(firstPhoto != null)
			value += SVList.dateformat.format(firstPhoto) + "; ";
		
		return value;
	}
	
	/* equal if name and num of photos are the same... */
	public boolean equals(Object o){
		if(o == null || !(o instanceof Album))
			return false;
		Album a = (Album)o;
		return a.name.equals(this.name);
	}
	
}
