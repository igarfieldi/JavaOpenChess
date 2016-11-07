package jchess;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class Localization {
	private final static ResourceBundle messages;
	
	static {
		messages = PropertyResourceBundle.getBundle("jchess.resources.i18n.main");
        Locale.setDefault(Locale.ENGLISH);
	}

    public static String getMessage(String key)
    {
        try
        {
            return Localization.messages.getString(key);
        }
        catch (java.util.MissingResourceException exc)
        {
        	return key;
        }
    }
}
