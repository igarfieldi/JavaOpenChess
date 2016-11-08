package jchess.util;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 * Adds wrapper methods to a ResourceBundle allowing typed access to its resources.
 * @author Florian Bethe
 *
 */
public class TypedResourceBundle {
	private ResourceBundle resource;
	
	/**
	 * Constructor. Creates a new ResourceBundle from the given name.
	 * @param baseName
	 */
	public TypedResourceBundle(String baseName) {
		resource = PropertyResourceBundle.getBundle(baseName);
	}
	
	/**
	 * Retrieves the string value corresponding to the key.
	 * @param key Key of the resource
	 * @return resource as string
	 */
	public String getString(String key) {
		return resource.getString(key);
	}
	
	/**
	 * Retrieves the integer value corresponding to the key.
	 * @param key Key of the resource
	 * @return resource as integer
	 */
	public Integer getInteger(String key) {
		return Integer.parseInt(resource.getString(key));
	}

	/**
	 * Retrieves the icon based on the resource name given.
	 * @param key Key of the resource
	 * @return icon resource
	 */
	public Icon getIcon(String key) {
		return new ImageIcon(resource.getString(key));
	}
}
