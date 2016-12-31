package usr;

import android.content.Context;

import io.DataIO;
import io.SVList;
import photo.Album;
import photo.Photo;

/**
 * @author Seong Bin Park
 * Main User object
 */
public class User extends Account{
	public User(String name){
		super(name);
		pathToData = SVList.USERPATH;
	}

	public boolean setAlbums(Context c, DataIO.IODataReader reader){
		this.albums = reader.readAlbum(c, this);
		if(albums == null)
			return false;
		return true;
	}

	public String toString(){
		String value = name + "\n";
		for(Album a: albums){
			value += a + "\n";
			for(Photo p: a.photos){
				value += p + "\n";
			}
		}
		
		return value;
	}
}
