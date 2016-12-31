package usr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import photo.Album;
import photo.Photo;
import photo.Tag;

/**
 * @author Seong Bin Park
 * Main Account, parent of Admin / User objects
 * All Accounts hold albums, because I said so.
 */
public class Account implements Serializable {
	public String name;
	public ArrayList<Album> albums;
	public String pathToData;
	
	public Account(String name){
		this.name = name;
		albums = new ArrayList<Album>();
	}
	
	public String getName(){return name;}

	public Album getAlbum(int position){
		if(position < albums.size())
			return albums.get(position);
		return null;
	}
	public Album getAlbum(String name){
		for(Album a: albums){
			if(a.name.equals(name))
				return a;
		}
		return null;
	}
	public boolean addAlbum(Album a){
		if(album_exists(a) || !albums.add(a))
			return false;

		Collections.sort(albums);
		return true;
	}
	public boolean removeAlbum(Album a){
		if(albums.size() < 1)
			return false;
		return albums.remove(a);
	}
	/**
	 * SEARCH
	 * @param type
	 * @param value
	 * @return ArrayList of Photos
	 * Main search is done here.
	 */
	public ArrayList<Photo> searchByTagPair(Tag.Type type, String value){
		if(albums.isEmpty())
			return null;
		
		ArrayList<Photo> searchPhoto = new ArrayList<Photo>();
		for(Album a: albums){
			for(Photo p: a.photos){
				for(Tag t: p.tags){
					if(t.search(type,value)){
						searchPhoto.add(p);
						break;
					}
				}
			}
		}
		
		return searchPhoto;
	}

	public boolean album_exists(Album a){
		for(Album album: albums){
			if(album.equals(a))
				return true;
		}
		return false;
	}
	public boolean album_exists(String name){
		for(Album album: albums){
			if(album.name.equals(name))
				return true;
		}
		return false;
	}
}
