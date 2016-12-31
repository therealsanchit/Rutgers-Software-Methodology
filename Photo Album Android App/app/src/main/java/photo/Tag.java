package photo;

import java.io.Serializable;

/**
 * @author Seong Bin Park
 * Main Tag object class
 */
public class Tag implements Serializable{
	private static final long serialVersionUID = 1L;

	public enum Type{
		PERSON, LOCATION, COLOR, MOOD
	};
	
	public String value;
	public Type type;
	
	public Tag(String[] tagSplits){
		this.type = Type.values()[Integer.parseInt(tagSplits[0])];
		this.value = tagSplits[1];
	}
	public Tag(Type t, String v){
		type = t;
		value = v;
	}
	
	public String toString(){return type + "; " + value;}

	public boolean search(Type t, String v){
		if(this.type != t)
			return false;
		if(!this.value.contains(v))
			return false;
		return true;
	}
	/**
	 * @param t
	 * @param v
	 * @return
	 * Two equals are to avoid making a whole instance of TAG when trying to search for tags.
	 * instead of making an instance (and having java to garbage-collect it later on) we can
	 * just pass in the parameters.
	 */
	public boolean equals(Type t, String v){
		return this.type == t && this.value.equals(v);
	}
	public boolean equals(Object o){
		if(o == null || !(o instanceof Tag))
			return false;
		Tag t = (Tag)o;
		return t.type == this.type && t.value.equals(this.value);
	}
}
