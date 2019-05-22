package fvarrui.makinito.i18n;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Permite recuperar los mensajes o textos mostrados por la aplicación. Gracias a esta clase
 * se consigue la internacionalización de la aplicación. 
 * 
 * @author Francisco Vargas
 *
 */
public class Messages {
	
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("i18n/messages");

	public static String getString(String key) {
		try {
			return BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			e.printStackTrace();
			return "!" + key + "!";
		}
	}
	
	public static String getString(String key, Object ... params) {
		try {
			return MessageFormat.format(BUNDLE.getString(key), params);
		} catch (MissingResourceException e) {
			e.printStackTrace();
			return "!" + key + "!";
		}
	}
	
}
