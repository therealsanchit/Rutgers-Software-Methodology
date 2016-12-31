package photo;

import java.util.Comparator;
import java.util.Date;

import io.SVList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Seong Bin Park
 * Each instance of ViewAlbum will hold an Album that it represents.
 * This is mainly because SimpleStringProperty was NOT serializable,
 * so it could not belong in class Album.
 */
public class ViewAlbum implements Comparable<ViewAlbum>{
	/*
	 * Viewing Class for album. Since Album should be serializable, we move
	 * Album's viewable data to here.
	 */
	public Album a;

	private SimpleStringProperty nameproperty;
	private SimpleStringProperty oldproperty;
	private SimpleStringProperty newproperty;
	private SimpleIntegerProperty intproperty;

	public ViewAlbum(Album a) {
		this.a = a;
		setAllProperties();
	}
	public void setAllProperties(){
		this.nameproperty = new SimpleStringProperty(a.name);
		if (a.firstPhoto == null)
			this.oldproperty = new SimpleStringProperty("empty");
		else
			this.oldproperty = new SimpleStringProperty(SVList.dateformat.format(a.firstPhoto));
		if (a.lastPhoto == null)
			this.newproperty = new SimpleStringProperty("empty");
		else
			this.newproperty = new SimpleStringProperty(SVList.dateformat.format(a.lastPhoto));
		this.intproperty = new SimpleIntegerProperty(a.numOfPhotos);
	}

	public String getNameproperty() {	return nameproperty.get();}
	public String getOldproperty() {	return oldproperty.get();}
	public String getNewproperty() {	return newproperty.get();}
	public int getIntproperty() {	return intproperty.get();}

	public void setNameproperty(String n) {	this.nameproperty.set(n);}
	public void setOldproperty(String o) {	this.oldproperty.set(o);}
	public void setNewproperty(String n) {	this.newproperty.set(n);}
	public void setIntproperty(int i) {	this.intproperty.set(i);}

	
	/**
	 * @param p
	 * When you add/delete, you need to use this to use Album's addToPhotos, or the list
	 * will never get updated. (ListView Column values depends on ViewAlbum)
	 */
	public void addPhotos(Photo p){
		a.addToPhotos(p);
		setAllProperties();
	}
	public void deletePhotos(Photo p){
		a.deleteFromPhotos(p);
		setAllProperties();
	}
	
	@Override
	public int compareTo(ViewAlbum arg0) {
		return this.a.compareTo(arg0.a);
	}
	
	public String toString() {
		return a.toString();
	}

	public boolean equals(Object o) {
		if (o == null || !(o instanceof ViewAlbum))
			return false;
		ViewAlbum va = (ViewAlbum) o;
		return va.a.equals(this.a);
	}

	

	

	
}
