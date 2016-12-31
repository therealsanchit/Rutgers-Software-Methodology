package photo;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.SVList;

/**
 * @author Seong Bin Park
 * Main Photo object class
 */
public class Photo implements Serializable{
	private static final long serialVersionUID = 3063112440307809632L;
	
	public String name;
	public String path;
	
	public ArrayList<Tag> tags;
	public int numOfTags;
	
	public Photo(String name, String path){
		this.name = name;
		this.path = path;
		this.tags = new ArrayList<Tag>();
	}
	
	public void editCaption(String name){
		this.name = name;
	}

	/**
	 * @param t
	 * @return true on success
	 * Always use these two methods to add/delete from tags.
	 */
	public boolean addToTags(Tag t){
		for(Tag tag: tags){
			if(tag.equals(t))
				return false;
		}
		tags.add(t);
		numOfTags = tags.size();
		return true;
	}
	public boolean deleteFromTags(Tag.Type t, String v){
		for(int x=0; x<tags.size(); x++){
			if(tags.get(x).equals(t,v)){
				tags.remove(x);
				numOfTags--;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @return String form of all the values concatenated with ", "
	 * I thought about making this to toString(), but I was using toString to test stuff.
	 */
	public String getTags(){
		String tags = "";
		for (int x = 0; x < this.numOfTags; x++) {
			if (x != 0)
				tags += ", ";
			tags += this.tags.get(x).value;
		}
		return tags;
	}
	public String toString(){
		String value =  name + "; " + path + "; " + numOfTags + "\n";
		if(tags != null && tags.size() > 0)
			for(Tag t: tags){
				value += t.toString();
				value += "\n";
			}
		return value;
	}
	
	public static Comparator<Photo> compareName() {
		return new Comparator<Photo>(){
			@Override
			public int compare(Photo arg0, Photo arg1){
				return arg0.name.compareTo(arg1.name);
			}
		};
	}
	
	public boolean equals(Object o){
		if(o == null || !(o instanceof Photo))
			return false;
		Photo p = (Photo) o;
		return p.path.equals(this.path) && p.name.equals(this.name);
	}
}
