package jchess.util;

public interface LoadSaveable
{
	/**
	 * Saves the current state to the given parser.
	 * @param parser Property map parser
	 */
	public void save(FileMapParser parser);

	/**
	 * Loads the state from the given parser.
	 * @param parser Property map parser
	 */
	public void load(FileMapParser parser);
}
