package usr;

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
		pathToData = SVList.USERPATH + "/" + name;
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
