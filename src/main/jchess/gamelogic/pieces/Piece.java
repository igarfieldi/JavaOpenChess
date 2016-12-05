/*
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * Authors:
 * Mateusz Sławomir Lach ( matlak, msl )
 * Damian Marciniak
 */
package jchess.gamelogic.pieces;

import java.awt.Image;
import java.util.Set;

import jchess.gamelogic.Player;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gui.ThemeImageLoader;
import jchess.util.Copyable;
import jchess.util.Direction;

/**
 * Class to represent a piece (any kind) - this class should be extended to
 * represent pawn, bishop etc.
 */
public abstract class Piece implements Copyable<Piece>
{
	// TODO: add possibility to ignore 'unit collision' ie. skip occupied fields?
	protected IChessboardController chessboard;
	protected Player player;
	private final String SYMBOL;
	private final boolean CAN_MOVE_MULTIPLE_STEPS;
	private boolean moved;
	
	public Piece(IChessboardController chessboard, Player player, String symbol)
	{
		this(chessboard, player, symbol, true);
	}

	public Piece(IChessboardController chessboard, Player player, String symbol, boolean multiMovePiece)
	{
		this.chessboard = chessboard;
		this.player = player;
		this.SYMBOL = symbol;
		this.CAN_MOVE_MULTIPLE_STEPS = multiMovePiece;
		this.moved = false;
	}
	
	/**
	 * Gets a set of directions in which the piece can move without capturing a unit.
	 * These do not necessarily have to be distinct (e.g. Bishop can both move
	 * and capture on both diagonals), but when applied the piece will only
	 * be able to move in this direction when no piece (regardless of friend
	 * or foe) currently blocks it.
	 * @return Set of non-capturing movement directions
	 */
	public abstract Set<Direction> getNormalMovements();
	
	/**
	 * Gets a set of directions in which the piece can move when capturing a unit.
	 * These do not necessarily have to be distinct (e.g. Bishop can both move
	 * and capture on both diagonals), but when applied the piece will only
	 * be able to move in this direction when an enemy piece currently blocks it.
	 * @return Set of capturing movement directions
	 */
	public abstract Set<Direction> getCapturingMovements();
	
	public final String getSymbol() {
		return SYMBOL;
	}
	
	public boolean canMoveMultipleSteps() {
		return CAN_MOVE_MULTIPLE_STEPS;
	}
	
	public boolean hasMoved() {
		return moved;
	}
	
	public void markAsMoved() {
		this.moved = true;
	}
	
	public void markAsUnmoved() {
		this.moved = false;
	}
	
	public Image getImage()
	{
		return ThemeImageLoader.getInstance().loadThemedPieceImage(this);
	}
	
	public String getName()
	{
		return this.getClass().getSimpleName();
	}

	public Player getPlayer()
	{
		return player;
	}
}
