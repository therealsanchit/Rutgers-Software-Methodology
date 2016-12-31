package photo;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import io.SVList;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @author Seong Bin Park
 * Each instance of ViewAlbum will hold a Photo that it represents.
 * This is mainly because SimpleStringProperty was NOT serializable,
 * so it could not belong in class Photo.
 */
public class ViewPhoto implements Comparable<ViewPhoto> {
	public Photo p;
	Image image;
	BufferedImage bimage;

	public SimpleStringProperty nameproperty;
	public SimpleStringProperty captionproperty;
	public SimpleStringProperty tagproperty;
	public SimpleStringProperty dateproperty;
	public SimpleObjectProperty<Image> thumbnailproperty;
	public SimpleObjectProperty<Image> imageproperty;

	/**
	 * @param p
	 * It's important to note that we are actually making and storing the WHOLE image 
	 * on creation of each photo object.
	 */
	public ViewPhoto(Photo p) {
		this.p = p;
		File f = new File(p.path);
		try {
			this.bimage = ImageIO.read(f);
			this.image = SwingFXUtils.toFXImage(bimage, null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		setAllProperties();
	}

	public void setAllProperties() {
		this.nameproperty = new SimpleStringProperty(p.name);
		this.captionproperty = new SimpleStringProperty(p.caption);
		String tags = "";
		for (int x = 0; x < p.numOfTags; x++) {
			if (x != 0)
				tags += ", ";
			tags += p.tags.get(x).value;
		}

		this.tagproperty = new SimpleStringProperty(tags);
		if (p.captured == null)
			this.dateproperty = new SimpleStringProperty("unknown");
		else
			this.dateproperty = new SimpleStringProperty(SVList.dateformat.format(p.captured));
		this.thumbnailproperty = new SimpleObjectProperty<Image>(getThumbnail());
		this.imageproperty = new SimpleObjectProperty<Image>(this.image);
	}

	/**
	 * @return small image
	 * This method returns the thumbnail of the image held by this object
	 */
	public Image getThumbnail() {
		java.awt.Image thumbimg = bimage.getScaledInstance(100, 100, BufferedImage.SCALE_SMOOTH);

		BufferedImage bufferedImage = new BufferedImage(thumbimg.getWidth(null), thumbimg.getHeight(null),
				BufferedImage.TYPE_INT_RGB);

		Graphics g = bufferedImage.createGraphics();
		g.drawImage(thumbimg, 0, 0, null);
		g.dispose();
		return SwingFXUtils.toFXImage(bufferedImage, null);
	}

	public String getNameproperty() {
		return nameproperty.get();
	}

	public String getCaptionproperty() {
		return captionproperty.get();
	}

	public String getTagproperty() {
		return tagproperty.get();
	}

	public String getDateproperty() {
		return dateproperty.get();
	}

	public ImageView getThumbnailproperty() {
		ImageView imageview = new ImageView(thumbnailproperty.get());
		return imageview;
	}

	public ImageView getImageproperty() {
		ImageView imageview = new ImageView(imageproperty.get());
		return imageview;
	}

	public void setNameproperty(String n) {
		this.nameproperty.set(n);
	}

	public void setCaptionproperty(String o) {
		this.captionproperty.set(o);
	}

	public void setTagproperty(String n) {
		this.tagproperty.set(n);
	}

	public void setDateproperty(String d) {
		this.dateproperty.set(d);
	}

	public void setThumbnailproperty(Image thumbnailproperty) {
		this.thumbnailproperty.set(thumbnailproperty);
	}

	public void setImageproperty(Image imageproperty) {
		this.thumbnailproperty.set(imageproperty);
	}

	/**
	 * @param t
	 * @return
	 * It's important to use this to add tags, or the ListView's Columns will never get updated
	 */
	public boolean addTags(Tag t) {
		if (p.addToTags(t)) {
			setAllProperties();
			return true;
		}
		return false;
	}

	public boolean deleteTags(Tag.Type t, String v) {
		if (p.deleteFromTags(t, v)) {
			setAllProperties();
			return true;
		}
		return false;
	}

	public void editCaption(String caption) {
		p.editCaption(caption);
		setAllProperties();
	}

	@Override
	public int compareTo(ViewPhoto arg0) {
		return Photo.compareName().compare(this.p, arg0.p);
	}

	public String toString() {
		return p.toString();
	}

	public boolean equals(Object o) {
		if (o == null || !(o instanceof ViewPhoto))
			return false;
		ViewPhoto vp = (ViewPhoto) o;
		return vp.p.equals(this.p);
	}

	public javafx.scene.image.Image getImage() {
		// TODO Auto-generated method stub
		return this.image;
	}
}
