package jchess.gamelogic.game;

import jchess.gamelogic.Player;

/**
 * Class representing a builder for game instances.
 * Depending on the concrete builder, different properties may be taken
 * into account for game construction (also different player counts).
 * @author Florian Bethe
 */
public interface IGameBuilder
{
	/**
	 * Adds a player to the game-to-be-constructed.
	 * @param player New player
	 */
	public void addPlayer(Player player);
	
	/**
	 * Sets the given property of the game-to-be-constructed.
	 * Any non-string property has to be casted during build time.
	 * @param key Property to be set
	 * @param value (New) value for the property
	 */
	public void setProperty(String key, String value);
	
	/**
	 * Creates a new game instance.
	 * Considers the added players and any set property.
	 * @return New game instance
	 */
	public IGame create();
}
