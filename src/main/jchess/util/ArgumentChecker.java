package jchess.util;

/**
 * Contains methods concerning the runtime checking of arguments.
 * @author Florian Bethe
 */
public class ArgumentChecker
{
	/**
	 * Checks if the given argument list contains a null object.
	 * If so, an IllegalArgumentException is thrown.
	 * @param args Arguments to be checked for nullity
	 */
	public static void checkForNull(Object ...args) {
		for(Object obj : args) {
			if(obj == null) {
				throw new IllegalArgumentException("Argument " + obj + " must not be null!");
			}
		}
	}
}
