package jchess.util;

public interface LoadSaveable
{
	/**
	 * Saves the current state to the given parser.
	 * @param parser Property map parser
	 * @return True if successful
	 */
	public boolean save(FileMapParser parser);

	/**
	 * Loads the state from the given parser.
	 * @param parser Property map parser
	 * @return True if successful
	 */
	public boolean load(FileMapParser parser);
}
