package jchess.gamelogic.controllers.chessboardcontrollers;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jchess.gamelogic.Player;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Move;
import jchess.gamelogic.field.Move.CastlingType;
import jchess.gamelogic.models.IBoardFactory;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.views.factories.IChessboardViewFactory;
import jchess.util.Direction;

public class AiFourPlayerChessboardController extends RegularChessboardController
{
	private static int WHITE_BASE_LINE = 13;
	private static int RED_BASE_LINE = 0;
	private static int BLACK_BASE_LINE = 0;
	private static int GOLDEN_BASE_LINE = 13;
	
	public AiFourPlayerChessboardController(IChessboardViewFactory viewFactory, IBoardFactory boardFactory,
	        Player white, Player red, Player black, Player golden, Player special)
	{
		super(viewFactory, boardFactory, Arrays.asList(new Player[]{ white, red, black, golden, special }));
		
	}
	
	@Override
	protected Move getRookMoveForCastling(Piece piece, CastlingType type)
	{
		Field field = getBoard().getField(piece);
		Field rookField = null;
		Field rookTarget = null;
		
		// Which rook gets to castle and where it should move depends on
		// the castling type and which player (ie. position on the board)
		// is castling
		if(field.getPosY() == BLACK_BASE_LINE || field.getPosY() == WHITE_BASE_LINE)
		{
			if(type == CastlingType.SHORT_CASTLING)
			{
				rookField = getBoard().getField(field.getPosX() + 3, field.getPosY());
				rookTarget = getBoard().getField(field.getPosX() + 1, field.getPosY());
			} else
			{
				rookField = getBoard().getField(field.getPosX() - 4, field.getPosY());
				rookTarget = getBoard().getField(field.getPosX() - 1, field.getPosY());
			}
		} else
		{
			if(type == CastlingType.SHORT_CASTLING)
			{
				rookField = getBoard().getField(field.getPosX(), field.getPosY() + 3);
				rookTarget = getBoard().getField(field.getPosX(), field.getPosY() + 1);
			} else
			{
				rookField = getBoard().getField(field.getPosX(), field.getPosY() - 4);
				rookTarget = getBoard().getField(field.getPosX(), field.getPosY() - 1);
			}
		}
		
		return new Move(rookField, rookTarget, getBoard().getPiece(rookField), null, type, false, null);
	}
	
	@Override
	protected Set<Move> getEnPassantMoves(Piece piece)
	{
		Set<Move> enPassantMoves = new HashSet<Move>();
		return enPassantMoves;
		
	}
	
	@Override
	protected boolean checkForPromotion(Piece pawn, Field target)
	{
		Direction forward = pawn.getBehaviour().getNormalMovements().iterator().next();
		
		// In 4p chess there are 4 different "base lines" where promotion
		// can happen
		if(forward.getX() == 0)
		{
			if(forward.getY() > 0)
			{
				return target.getPosY() == WHITE_BASE_LINE;
			} else
			{
				return target.getPosY() == BLACK_BASE_LINE;
			}
		} else
		{
			if(forward.getX() > 0)
			{
				return target.getPosX() == GOLDEN_BASE_LINE;
			} else
			{
				return target.getPosX() == RED_BASE_LINE;
			}
		}
	}
}
