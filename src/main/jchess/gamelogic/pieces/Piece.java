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

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jchess.gamelogic.Player;
import jchess.gamelogic.field.ChessboardController;
import jchess.gamelogic.field.Field;
import jchess.gui.ThemeImageLoader;
import jchess.util.Direction;

import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * Class to represent a piece (any kind) - this class should be extended to
 * represent pawn, bishop etc.
 */
public abstract class Piece
{
	
	public ChessboardController chessboard; // <-- this relation isn't in class diagram,
	                              // but it's necessary :/
	public Field square;
	public Player player;
	protected String symbol;
	
	public abstract List<Direction> getNormalMovements();
	public abstract List<Direction> getStrikingMovements();
	
	public Image getImage() {
		return ThemeImageLoader.loadThemedPieceImage(this);
	}
	
	public String getName() {
		return this.getClass().getSimpleName();
	}
	
	Piece(ChessboardController chessboard, Player player)
	{
		this.chessboard = chessboard;
		this.player = player;
		
	}
	/*
	 * Method to draw piece on chessboard
	 * 
	 * @graph : where to draw
	 */
	
	void clean()
	{
	}
	
	/**
	 * method check if Piece can move to given square
	 * 
	 * @param square
	 *            square where piece want to move (Square object)
	 * @param possibleMoves
	 *            all moves which can piece do
	 */
	boolean canMove(Field square, ArrayList<?> allmoves)
	{
		// throw new UnsupportedOperationException("Not supported yet.");
		ArrayList<?> moves = allmoves;
		for(Iterator<?> it = moves.iterator(); it.hasNext();)
		{
			Field sq = (Field) it.next();// get next from iterator
			if(sq == square)
			{// if address is the same
				return true; // piece canMove
			}
		}
		return false;// if not, piece cannot move
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
	protected boolean isout(int x, int y)
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
		if(chessboard.getBoard().getField(x, y).getPiece() != null && chessboard.getBoard().getField(x, y).getPiece().getName().equals("King"))
		{
			return false;
		}
		Piece piece = chessboard.getBoard().getField(x, y).getPiece();
		if(piece == null || // if this square is empty
		        piece.player != this.player) // or piece is another player
		{
			return true;
		}
		return false;
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
	
	public String getSymbol()
	{
		return this.symbol;
	}
}
