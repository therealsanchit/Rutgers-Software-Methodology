package photo;

import java.awt.Image;
import java.awt.image.BufferedImage;
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

import javax.imageio.ImageIO;

import io.SVList;

/**
 * @author Seong Bin Park
 * Main Photo object class
 */
public class Photo implements Serializable{
	private static final long serialVersionUID = 3063112440307809632L;
	
	public String name;
	public String path;
	public String caption;
	public Date captured;
	
	public ArrayList<Tag> tags;
	public int numOfTags;
	
	public Photo(String name, String caption, String path){
		this.name = name;
		this.caption = caption;
		this.path = path;
		this.tags = new ArrayList<Tag>();
		/* assumes that path is already checked to be valid */
		File f = new File(path);
		
		this.captured = new Date(f.lastModified());
	}

	public String getDateOfCaptured(){
		return SVList.dateformat.format(captured);
	}
	
	public void editCaption(String caption){
		this.caption = caption;
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
		String value =  name + "; " + path + "; " + caption + "; " + numOfTags + "; " + SVList.dateformat.format(captured) + "\n";
		if(tags != null && tags.size() > 0)
			for(Tag t: tags){
				value += t.toString();
				value += "\n";
			}
		return value;
	}

	/**
	 * @param path
	 * @return true on success
	 * findValidPath will find if the path is 
	 * 1. valid,
	 * 2. is pointing to a picture file 
	 */
	public static boolean findValidPath(String path){
		File f = new File(path);
		if(!f.exists())
			return false;
		
		try {
			if(ImageIO.read(f) == null){
				return false;
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	public static Comparator<Photo> compareDate() {
		return new Comparator<Photo>(){
			@Override
			public int compare(Photo arg0, Photo arg1) {
				return arg0.captured.compareTo(arg1.captured);
			}
		};
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
