package io;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * @author kidoc_000
 * SVList will contain all of the static final information
 */
public class SVList {
	public final static String USERPATH = "/save";
	public final static String TEMP = "/temp";
	
	public final static DateFormat dateformat = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
	
	public final static String[] TAG_TYPES = {"PERSON", "LOCATION", "COLOR", "MOOD"};
}
