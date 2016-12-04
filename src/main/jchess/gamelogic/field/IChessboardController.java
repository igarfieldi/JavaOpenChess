package jchess.gamelogic.field;

import java.util.Set;

import jchess.gamelogic.Player;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.views.IChessboardView;

public interface IChessboardController
{
	
	public IChessboardView getView();
	public void setView(IChessboardView view);
	
	public Player getActivePlayer();
	
	public void switchToNextPlayer();
	
	public void switchToPreviousPlayer();
	
	/**
	 * Returns the string representation of the given field.
	 * 
	 * @param field
	 * @return
	 */
	public String getFieldDesignation(Field field);
	
	/**
	 * Returns the field specified by the given string, if applicable.
	 * 
	 * @param designation
	 *            Designation of desired field
	 * @return field denoted by given string (or null if non-existent)
	 */
	public Field getFieldFromDesignation(String designation);
	
	/**
	 * Brings the controller (and the associated board) into a clean state.
	 */
	public void initialize();
	
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
	 *            square where we want to move piece *
	 * @param refresh
	 *            chessboard, default: true
	 */
	public void move(Field begin, Field end, boolean refresh, boolean clearForwardHistory, boolean enterIntoHistory);
	
	public boolean redo();
	
	public boolean undo();
	
	public IChessboardModel getBoard();
	
}