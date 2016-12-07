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
package jchess.gamelogic.controllers.chessboardcontrollers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jchess.gamelogic.Player;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.pieces.Bishop;
import jchess.gamelogic.pieces.King;
import jchess.gamelogic.pieces.Knight;
import jchess.gamelogic.pieces.Pawn;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.pieces.Queen;
import jchess.gamelogic.pieces.Rook;
import jchess.gamelogic.views.IChessboardView;
import jchess.util.Direction;

/**
 * Class to represent chessgetBoard(). Chessboard is made from squares. It is setting
 * the squers of chessboard and sets the pieces(pawns) witch the owner is
 * current player on it.
 */
public class FourPlayerChessboardController extends RegularChessboardController
{	
	public FourPlayerChessboardController(IChessboardView view, IChessboardModel board,
			Player white, Player red, Player black, Player golden)
	{
		super(view, board, Arrays.asList(new Player[]{white, red, black, golden}));
	}
	
	@Override
	protected void initializePieces()
	{
		getBoard().initialize();
		// Set rooks, bishops, knights, queen, king
		getBoard().setPiece(getBoard().getField(3, 13), new Rook(getPlayer(0)));
		getBoard().setPiece(getBoard().getField(10, 13), new Rook(getPlayer(0)));
		getBoard().setPiece(getBoard().getField(4, 13), new Knight(getPlayer(0)));
		getBoard().setPiece(getBoard().getField(9, 13), new Knight(getPlayer(0)));
		getBoard().setPiece(getBoard().getField(5, 13), new Bishop(getPlayer(0)));
		getBoard().setPiece(getBoard().getField(8, 13), new Bishop(getPlayer(0)));
		getBoard().setPiece(getBoard().getField(6, 13), new Queen(getPlayer(0)));
		getBoard().setPiece(getBoard().getField(7, 13), new King(getPlayer(0)));
		
		getBoard().setPiece(getBoard().getField(0, 10), new Rook(getPlayer(1)));
		getBoard().setPiece(getBoard().getField(0, 3), new Rook(getPlayer(1)));
		getBoard().setPiece(getBoard().getField(0, 9), new Knight(getPlayer(1)));
		getBoard().setPiece(getBoard().getField(0, 4), new Knight(getPlayer(1)));
		getBoard().setPiece(getBoard().getField(0, 8), new Bishop(getPlayer(1)));
		getBoard().setPiece(getBoard().getField(0, 5), new Bishop(getPlayer(1)));
		getBoard().setPiece(getBoard().getField(0, 7), new Queen(getPlayer(1)));
		getBoard().setPiece(getBoard().getField(0, 6), new King(getPlayer(1)));
		
		getBoard().setPiece(getBoard().getField(3, 0), new Rook(getPlayer(2)));
		getBoard().setPiece(getBoard().getField(10, 0), new Rook(getPlayer(2)));
		getBoard().setPiece(getBoard().getField(4, 0), new Knight(getPlayer(2)));
		getBoard().setPiece(getBoard().getField(9, 0), new Knight(getPlayer(2)));
		getBoard().setPiece(getBoard().getField(5, 0), new Bishop(getPlayer(2)));
		getBoard().setPiece(getBoard().getField(8, 0), new Bishop(getPlayer(2)));
		getBoard().setPiece(getBoard().getField(6, 0), new Queen(getPlayer(2)));
		getBoard().setPiece(getBoard().getField(7, 0), new King(getPlayer(2)));

		getBoard().setPiece(getBoard().getField(13, 10), new Rook(getPlayer(3)));
		getBoard().setPiece(getBoard().getField(13, 3), new Rook(getPlayer(3)));
		getBoard().setPiece(getBoard().getField(13, 9), new Knight(getPlayer(3)));
		getBoard().setPiece(getBoard().getField(13, 4), new Knight(getPlayer(3)));
		getBoard().setPiece(getBoard().getField(13, 8), new Bishop(getPlayer(3)));
		getBoard().setPiece(getBoard().getField(13, 5), new Bishop(getPlayer(3)));
		getBoard().setPiece(getBoard().getField(13, 6), new Queen(getPlayer(3)));
		getBoard().setPiece(getBoard().getField(13, 7), new King(getPlayer(3)));
		
		// Initialize pawns: no special distinctions necessary
		for(int x = 3; x < 11; x++)
		{
			getBoard().setPiece(getBoard().getField(x, 12), new Pawn(getPlayer(0), new Direction(0, -1)));
			getBoard().setPiece(getBoard().getField(x, 1), new Pawn(getPlayer(2), new Direction(0, 1)));
		}
		for(int y = 3; y < 11; y++)
		{
			getBoard().setPiece(getBoard().getField(1, y), new Pawn(getPlayer(1), new Direction(1, 0)));
			getBoard().setPiece(getBoard().getField(12, y), new Pawn(getPlayer(3), new Direction(-1, 0)));
		}
	}
	
	protected Set<Field> getCastleMoves(Piece piece) {
		Set<Field> castleFields = new HashSet<Field>();
		
		// TODO: proper castling implementation
		/*if(piece instanceof King)
		{
			// Castling
			if(!piece.hasMoved())
			{
				Field kingsField = getBoard().getField(piece);
				
				Piece leftRook = getBoard().getPiece(getBoard().getField(0, kingsField.getPosY()));
				Piece rightRook = getBoard().getPiece(getBoard().getField(7, kingsField.getPosY()));
				
				if(leftRook != null && !leftRook.hasMoved())
				{
					// Neither left rook nor king have moved yet
					Set<Field> reachable = this.getMovableFieldsInDirection(leftRook, leftRook.getNormalMovements());
					if(reachable.contains(getBoard().getField(kingsField.getPosX() - 1, kingsField.getPosY())))
					{
						// Left rook can move right next to the king -> fields
						// in between both are free
						Set<Field> involvedFields = new HashSet<Field>();
						involvedFields.add(kingsField);
						involvedFields.add(getBoard().getField(kingsField.getPosX() - 1, kingsField.getPosY()));
						involvedFields.add(getBoard().getField(kingsField.getPosX() - 2, kingsField.getPosY()));
						// None of the fields involved must be in check
						
						Player enemy = getPlayer(0);
						if(piece.getPlayer() == getPlayer(0))
						{
							enemy = getPlayer(1);
						}
						if(!this.isAnyThreatenedByPlayer(involvedFields, enemy))
						{
							castleFields.add(getBoard().getField(kingsField.getPosX() - 2, kingsField.getPosY()));
						}
					}
				}
				if(rightRook != null && !rightRook.hasMoved())
				{
					// Neither left rook nor king have moved yet
					Set<Field> reachable = this.getMovableFieldsInDirection(rightRook, rightRook.getNormalMovements());
					if(reachable.contains(getBoard().getField(kingsField.getPosX() + 1, kingsField.getPosY())))
					{
						// Right rook can move right next to the king -> fields
						// in between both are free
						Set<Field> involvedFields = new HashSet<Field>();
						involvedFields.add(kingsField);
						involvedFields.add(getBoard().getField(kingsField.getPosX() + 1, kingsField.getPosY()));
						involvedFields.add(getBoard().getField(kingsField.getPosX() + 2, kingsField.getPosY()));
						// None of the fields involved must be in check
						
						Player enemy = getPlayer(0);
						if(piece.getPlayer() == getPlayer(0))
						{
							enemy = getPlayer(1);
						}
						if(!this.isAnyThreatenedByPlayer(involvedFields, enemy))
						{
							castleFields.add(getBoard().getField(kingsField.getPosX() + 2, kingsField.getPosY()));
						}
					}
				}
			}
		}*/
		
		return castleFields;
	}
	
	@Override
	protected Set<Field> getEnPassantMoves(Piece piece)
	{
		Set<Field> enPassantMoves = new HashSet<Field>();
		
		// TODO: proper En Passant implementation
		/*if(piece instanceof Pawn)
		{
			Move lastMove = getHistory().getLastMoveFromHistory();
			// En passant
			if(lastMove != null && lastMove.wasPawnTwoFieldsMove())
			{
				Pawn twoSquareMovedPawn = (Pawn) lastMove.getMovedPiece();
				if(getBoard().getField(piece).getPosY() == getBoard().getField(twoSquareMovedPawn).getPosY())
				{
					// Our pawn is in the same row
					if(Math.abs(getBoard().getField(piece).getPosX() - getBoard().getField(twoSquareMovedPawn).getPosX()) == 1)
					{
						// Our pawn is right next to the pawn which moved two
						// squares. Now we need to check the direction in which
						// the pawn has to go
						if(piece.getPlayer() == getPlayer(1))
						{
							enPassantMoves.add(getBoard().getField(getBoard().getField(twoSquareMovedPawn).getPosX(),
							        getBoard().getField(piece).getPosY() + 1));
						} else
						{
							enPassantMoves.add(getBoard().getField(getBoard().getField(twoSquareMovedPawn).getPosX(),
							        getBoard().getField(piece).getPosY() - 1));
						}
					}
				}
			}
		}*/
		
		return enPassantMoves;
	}
}
