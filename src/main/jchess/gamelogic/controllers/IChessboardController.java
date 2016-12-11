package jchess.gamelogic.controllers;

import java.util.Set;

import jchess.gamelogic.Player;
import jchess.gamelogic.controllers.chessboardcontrollers.IllegalMoveException;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.History;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.views.IChessboardView;
import jchess.util.FileMapParser;

public interface IChessboardController
{
	
	public void save(FileMapParser parser);
	
	public void loadFromString(String str);
	
	public IChessboardView getView();
	
	public IChessboardModel getBoard();
	
	public History getHistory();
	
	public Player getActivePlayer();
	
	public void switchToNextPlayer();
	
	public void switchToPreviousPlayer();
	
	/**
	 * Returns the set of fields a piece can move to. This includes both
	 * capturing and non-capturing moves as well as special cases (e.g. castling
	 * or en passant capturing). If specified, the set will also be cleaned from
	 * fields which would result in a check position (or don't resolve a
	 * currently present one) if moved to by the piece.
	 * 
	 * @param piece
	 *            Piece to get moves for
	 * @param careForCheck
	 *            if true, moves resulting in a check will be filtered out
	 * @return Set of possible moves for the piece
	 */
	public Set<Field> getPossibleMoves(Piece piece, boolean careForCheck);
	
	/**
	 * Returns the set of fields which currently threaten the given piece.
	 * @param piece Piece under threat
	 * @param careForCheck Should moves resulting in check be filtered out or not
	 * @return Set of threatening fields
	 */
	public Set<Field> getPossibleThreats(Piece piece, boolean careForCheck);
	
	/**
	 * Checks if the player is currently in a check position.
	 * 
	 * @param player
	 *            Player to be checked
	 * @return true if in check
	 */
	public boolean isChecked(Player player);
	
	/**
	 * Checks if the player is checkmated. This is equivalent to checking if the
	 * player is checked and does not have any possible moves.
	 * 
	 * @param player
	 *            Player to be checked for
	 * @return true if checkmated
	 */
	public boolean isCheckmated(Player player);
	
	public boolean isStalemate();
	
	/**
	 * Method move piece from square to square
	 * 
	 * @param begin
	 *            square from which move piece
	 * @param end
	 *            square where we want to move piece
	 * @throws IllegalMoveException if the proposed move is not valid
	 */
	public boolean move(Field begin, Field end) throws IllegalMoveException;
	
}