package jchess.gamelogic.field;

public interface IBoardActionHandler
{
	public void onFieldSelection(Field field);
	public void onRedoRequested();
	public void onUndoRequested();
}
