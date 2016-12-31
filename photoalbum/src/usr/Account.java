package usr;

import java.util.ArrayList;
import java.util.Date;

import photo.Album;
import photo.Photo;
import photo.Tag;

/**
 * @author Seong Bin Park
 * Main Account, parent of Admin / User objects
 * All Accounts hold albums, because I said so.
 */
public class Account {
	public String name;
	public ArrayList<Album> albums;
	public String pathToData;
	
	public Account(String name){
		this.name = name;
		albums = new ArrayList<Album>();
	}
	
	public String getName(){return name;}
	
	/**
	 * SEARCH
	 * @param early
	 * @param late
	 * @return ArrayList of Photos
	 * Main search is done here.
	 */
	public ArrayList<Photo> searchByDateRange(Date early, Date late){
		if(albums.isEmpty())
			return null;
		
		ArrayList<Photo> searchPhoto = new ArrayList<Photo>();
		for(Album a: albums){
			for(Photo p: a.photos){
				if(p.captured.compareTo(early) >= 0 && p.captured.compareTo(late) <= 0)
					searchPhoto.add(p);
			}
		}
		
		return searchPhoto;
	}
	
	public ArrayList<Photo> searchByTagPair(Tag.Type type, String value){
		if(albums.isEmpty())
			return null;
		
		ArrayList<Photo> searchPhoto = new ArrayList<Photo>();
		for(Album a: albums){
			for(Photo p: a.photos){
				for(Tag t: p.tags){
					if(t.equals(type,value)){
						searchPhoto.add(p);
						break;
					}
				}
			}
		}
		
		return searchPhoto;
	}
}
