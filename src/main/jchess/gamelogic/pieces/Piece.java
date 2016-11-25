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
import java.util.ArrayList;
import java.util.List;

import jchess.gamelogic.Player;
import jchess.gamelogic.field.ChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gui.ThemeImageLoader;
import jchess.util.Direction;

/**
 * Class to represent a piece (any kind) - this class should be extended to
 * represent pawn, bishop etc.
 */
public abstract class Piece
{
	// TODO: add possibility to ignore 'unit collision' ie. skip occupied fields?
	protected ChessboardController chessboard;
	private Field square;
	private Player player;
	private final String SYMBOL;
	private final boolean CAN_MOVE_MULTIPLE_STEPS;
	
	public Piece(ChessboardController chessboard, Player player, String symbol)
	{
		this(chessboard, player, symbol, true);
	}

	public Piece(ChessboardController chessboard, Player player, String symbol, boolean multiMovePiece)
	{
		this.chessboard = chessboard;
		this.player = player;
		this.SYMBOL = symbol;
		this.CAN_MOVE_MULTIPLE_STEPS = multiMovePiece;
	}
	
	public abstract List<Direction> getNormalMovements();
	public abstract List<Direction> getCapturingMovements();
	
	public final String getSymbol() {
		return SYMBOL;
	}
	
	public boolean canMoveMultipleSteps() {
		return CAN_MOVE_MULTIPLE_STEPS;
	}
	
	/**
	 * Checks whether this piece currently threatens a field or not.
	 * @param target Field to check
	 * @return field threatened or not
	 */
	public boolean threatens(Field target) {
		// Iterate all threatened fields and check if ours is in there
		for(Field threatenedField : this.getThreatenedFields()) {
			if(threatenedField.equals(target)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns a list of all board fields which are currently threatened by this piece.
	 * @return list of threatened fields
	 */
	private List<Field> getThreatenedFields() {
		List<Field> capturableFields = new ArrayList<Field>();
		
		// Iterate all capturing directions for this piece
		for(Direction dir : this.getCapturingMovements()) {
			List<Field> fieldsInDir = new ArrayList<Field>();
			if(this.CAN_MOVE_MULTIPLE_STEPS) {
				// Multiple possible fields to check for direction
				fieldsInDir.addAll(chessboard.getBoard().getFieldsInDirection(this.square, dir));
			} else {
				// Only one possible field for given direction
				fieldsInDir.add(chessboard.getBoard().getFieldInDirection(this.square, dir));
			}
			
			// Since multiple fields are possible, check if some are 'hidden' behind pieces
			for(Field fieldInDir : fieldsInDir) {
				Piece piece = chessboard.getBoard().getPiece(fieldInDir);
				
				if(piece != null) {
					// Piece blocks. Question: ours or not?
					if(piece.player != this.player) {
						capturableFields.add(fieldInDir);
					}
					// Break necessary anyway; in chess, pieces cannot "skip"
					// fields with pieces on it
					break;
				} else {
					// No piece? Capturable
					capturableFields.add(fieldInDir);
				}
			}
		}
		
		return capturableFields;
	}
	
	// void setImages(String white, String black) {
	/*
	 * method set image to black or white (depends on player colour)
	 * 
	 * @white: String with name of image with white piece
	 * 
	 * @black: String with name of image with black piece
	 */
	// this.imageBlack = black;
	// this.imageWhite = white;
	// if(player.color == player.color.black) {
	// this.image = GUI.loadImage(imageBlack);
	// } else {
	// this.image = GUI.loadImage(imageWhite);
	// }
	// }/*--endOf-setImages(String white, String black)--*/
	
	abstract public ArrayList<Field> possibleMoves();
	
	/**
	 * Method is useful for out of bounds protection
	 * 
	 * @param x
	 *            x position on chessboard
	 * @param y
	 *            y position on chessboard
	 * @return true if parameters are out of bounds (array)
	 */
	protected static boolean isout(int x, int y)
	{
		if(x < 0 || x > 7 || y < 0 || y > 7)
		{
			return true;
		}
		return false;
	}
	
	/**
	 * @param x
	 *            y position on chessboard
	 * @param y
	 *            y position on chessboard
	 * @return true if can move, false otherwise
	 */
	protected boolean checkPiece(int x, int y)
	{
		Piece piece = chessboard.getBoard().getField(x, y).getPiece();
		if((piece != null) && (piece instanceof King))
		{
			return false;
		} else if((piece == null) || (piece.player != this.player))
		{
			return true;
		} else
		{
			return false;
		}
	}
	
	/**
	 * Method check if piece has other owner than calling piece
	 * 
	 * @param x
	 *            x position on chessboard
	 * @param y
	 *            y position on chessboard
	 * @return true if owner(player) is different
	 */
	protected boolean otherOwner(int x, int y)
	{
		Field sq = chessboard.getBoard().getField(x, y);
		if(sq.getPiece() == null)
		{
			return false;
		}
		if(this.player != sq.getPiece().player)
		{
			return true;
		}
		return false;
	}
	
	public Image getImage()
	{
		return ThemeImageLoader.loadThemedPieceImage(this);
	}
	
	public String getName()
	{
		return this.getClass().getSimpleName();
	}

	public Player getPlayer()
	{
		return player;
	}
	public Field getSquare()
	{
		return square;
	}
	public void setSquare(Field square)
	{
		this.square = square;
	}
}
