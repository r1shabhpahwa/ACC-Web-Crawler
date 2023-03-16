package services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class for Web Crawler and HTML to Text functionality
 *
 * @author (Rishabh Pahwa)
 */
public class URLValidation {

	public static final String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

	/**
	 * Method to check if input URL is valid
	 * 
	 * @param url (URL to validate)
	 * @return boolean 
	 */
	public boolean isValidURL(String url) {

		// Match input with regular expression
		try {
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(url);
			return matcher.matches();
		} catch (RuntimeException e) {
			return false;
		}
	}
}

