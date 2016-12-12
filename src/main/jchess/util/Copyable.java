package jchess.util;

/**
 * Provides a way to perform deep copies.
 * Unlike Java's Cloneable, no standard implementation is provided
 * so users are guaranteed that a custom implementation exists.
 * @author Florian Bethe
 *
 * @param <K> Class which should be copied
 */
public interface Copyable<K>
{
	/**
	 * Provides a deep(!) copy of the current instance.
	 * @return Copied instance
	 */
	public K copy();
}
