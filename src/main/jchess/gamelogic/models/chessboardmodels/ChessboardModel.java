package jchess.gamelogic.models.chessboardmodels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jchess.gamelogic.Player;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.pieces.Piece;
import jchess.util.ArgumentChecker;
import jchess.util.BiMap;
import jchess.util.Direction;

public class ChessboardModel implements IChessboardModel
{
	private Map<Integer, Map<Integer, Field>> fields;
	private Set<Field> allFields;
	private BiMap<Field, Piece> pieces;
	
	public ChessboardModel()
	{
		
		this.fields = new HashMap<Integer, Map<Integer, Field>>();
		this.allFields = new HashSet<Field>();
		this.pieces = new BiMap<Field, Piece>();
	}
	
	@Override
	public void addField(Field field)
	{
		if(allFields.add(field))
		{
			// Only do expensive lookup when the element is not already present
			Map<Integer, Field> yMap = fields.get(field.getPosX());
			if(yMap == null)
			{
				yMap = new HashMap<Integer, Field>();
				fields.put(field.getPosX(), yMap);
			}
			yMap.put(field.getPosY(), field);
		}
	}
	
	@Override
	public Field getField(int x, int y)
	{
		Map<Integer, Field> yMap = fields.get(x);
		if(yMap == null)
		{
			return null;
		}
		
		return yMap.get(y);
	}
	
	@Override
	public Field getField(Piece piece)
	{
		return pieces.inverse().get(piece);
	}
	
	@Override
	public Set<Field> getFields()
	{
		return this.allFields;
	}
	
	@Override
	public List<Field> getEmptyFields()
	{
		List<Field> emptyFields = new ArrayList<Field>();
		for(Field field : getFields())
		{
			if(getPiece(field) == null)
			{
				emptyFields.add(field);
			}
		}
		return emptyFields;
	}
	
	@Override
	public Field getFieldInDirection(Field origin, Direction dir)
	{
		ArgumentChecker.checkForNull(origin, dir);
		
		try
		{
			return this.getField(origin.getPosX() + dir.getX(), origin.getPosY() + dir.getY());
		} catch(ArrayIndexOutOfBoundsException exc)
		{
			return null;
		}
	}
	
	@Override
	public List<Field> getFieldsInDirection(Field start, Direction dir)
	{
		ArgumentChecker.checkForNull(start, dir);
		
		List<Field> directionFields = new ArrayList<Field>();
		// Iterate through all fields in the given direction (until we hit null
		// which means we're off the board)
		Field currField = start;
		while((currField = this.getFieldInDirection(currField, dir)) != null)
		{
			directionFields.add(currField);
		}
		
		return directionFields;
	}
	
	@Override
	public Set<Piece> getPieces()
	{
		return new HashSet<Piece>(pieces.values());
	}
	
	@Override
	public Set<Piece> getPieces(Player player)
	{
		Set<Piece> playerPieces = new HashSet<Piece>();
		
		for(Piece piece : pieces.values())
		{
			if(piece.getPlayer() == player)
			{
				playerPieces.add(piece);
			}
		}
		
		return playerPieces;
	}

	
	@Override
	public Piece getPiece(Field field)
	{
		return pieces.get(field);
	}
	
	@Override
	public void setPiece(Field field, Piece piece)
	{
		ArgumentChecker.checkForNull(field);
		pieces.put(field, piece);
	}
	
	@Override
	public Piece movePiece(Piece piece, Field target)
	{
		return pieces.put(target, piece);
	}
	
	@Override
	public Piece removePiece(Field field)
	{
		return pieces.remove(field);
	}
	
	@Override
	public IChessboardModel copy()
	{
		ChessboardModel board = new ChessboardModel();
		for(Field field : this.getFields())
		{
			Field copy = new Field(field);
			board.addField(copy);
			Piece piece = this.getPiece(field);
			if(piece != null)
			{
				board.setPiece(copy, piece.copy());
			}
		}
		return board;
	}
}
