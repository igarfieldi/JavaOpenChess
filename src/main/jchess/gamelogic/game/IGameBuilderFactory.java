package jchess.gamelogic.game;

/**
 * Factory which allows different builders to be used.
 * @author Florian Bethe
 */
public interface IGameBuilderFactory
{
	/**
	 * Returns a builder for use.
	 * @return New builder
	 */
	public IGameBuilder getBuilder();
}
