package jchess.gamelogic.models;

import java.util.List;
import java.util.Set;

import jchess.gamelogic.Player;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.pieces.Piece;
import jchess.util.Copyable;
import jchess.util.Direction;

public interface IChessboardModel extends Copyable<IChessboardModel>
{
	
	public IChessboardModel copy();
	
	public void addField(Field field);
	
	public Field getField(int x, int y);
	
	public Field getField(Piece piece);
	
	public Piece getPiece(Field field);
	
	public void setPiece(Field field, Piece piece);
	
	public Piece movePiece(Piece piece, Field target);
	
	public Piece removePiece(Field field);
	
	public Set<Field> getFields();
	
	public Field getFieldInDirection(Field origin, Direction dir);
	
	public List<Field> getFieldsInDirection(Field start, Direction dir);
	
	public Set<Piece> getPieces();
	
	public Set<Piece> getPieces(Player player);
	
}