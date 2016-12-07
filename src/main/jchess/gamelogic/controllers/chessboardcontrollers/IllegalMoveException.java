package jchess.gamelogic.controllers.chessboardcontrollers;

public class IllegalMoveException extends Exception
{
	private static final long serialVersionUID = -8439159363353723951L;

	public IllegalMoveException(String what) {
		super(what);
	}
}
