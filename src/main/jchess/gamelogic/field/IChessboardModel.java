package jchess.gamelogic.field;

import java.util.List;
import java.util.Set;

import jchess.gamelogic.Player;
import jchess.gamelogic.pieces.Piece;
import jchess.util.Copyable;
import jchess.util.Direction;

public interface IChessboardModel extends Copyable<IChessboardModel>
{
	
	void initialize();
	
	IChessboardModel copy();
	
	Field getField(int x, int y);
	
	Field getField(Piece piece);
	
	Set<Field> getFields();
	
	Field getFieldInDirection(Field origin, Direction dir);
	
	List<Field> getFieldsInDirection(Field start, Direction dir);
	
	Set<Piece> getPieces();
	
	Set<Piece> getPieces(Player player);
	
	Piece getPiece(Field field);
	
	void setPiece(Field field, Piece piece);
	
	Piece movePiece(Piece piece, Field target);
	
	Piece removePiece(Field field);
	
}