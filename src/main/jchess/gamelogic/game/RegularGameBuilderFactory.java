package jchess.gamelogic.game;

/**
 * Factory for regular builder.
 * @author Florian Bethe
 */
public class RegularGameBuilderFactory implements IGameBuilderFactory
{
	private static RegularGameBuilderFactory instance;
	
	/**
	 * Returns instance of the factory.
	 * @return instance
	 */
	public static RegularGameBuilderFactory getInstance() {
		if(instance == null) {
			instance = new RegularGameBuilderFactory();
		}
		
		return instance;
	}
	
	@Override
	public IGameBuilder getBuilder() {
		return new TimedGameBuilder();
	}
}
