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
import usr.User;

/**
 * @author Seong Bin Park
 * Main Album object class
 */
public class Album implements Serializable, Comparable<Album>{
	private static final long serialVersionUID = -5903254468625127378L;

	public String name;
	public String photoTXT;
	public int numOfPhotos;
	public ArrayList<Photo> photos;

	public Album(ArrayList<Photo> p){
		photos = p;
		numOfPhotos = p.size();
	}
	public Album(String name){
		this.name = name;
		this.photoTXT = "";
		this.numOfPhotos = 0;
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
	}
	public boolean deleteFromPhotos(Photo p){
		for(int x=0; x<photos.size(); x++){
			if(photos.get(x).equals(p)){
				photos.remove(x);
				numOfPhotos--;
				return true;
			}
		}
		return false;
	}
	public List<Photo> getPhotos(){
		return photos;
	}
	public Photo getPhoto(int position){
		if(position < photos.size())
			return photos.get(position);
		return null;
	}
	public boolean findPhoto(String path){
		for(Photo p: photos){
			if(path.equals(p.path))
				return true;
		}
		return false;
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
