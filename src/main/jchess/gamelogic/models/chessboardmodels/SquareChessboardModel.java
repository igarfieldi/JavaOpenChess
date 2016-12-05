package jchess.gamelogic.models.chessboardmodels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jchess.gamelogic.Player;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.models.IChessboardModel;
import jchess.gamelogic.pieces.Piece;
import jchess.util.ArgumentChecker;
import jchess.util.BiMap;
import jchess.util.Direction;

public abstract class SquareChessboardModel implements IChessboardModel
{
	private final int WIDTH;
	private final int HEIGHT;
	private Field[][] fields;
	private BiMap<Field, Piece> pieces;
	
	public SquareChessboardModel(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.fields = new Field[WIDTH][HEIGHT];
		this.pieces = new BiMap<Field, Piece>();
	}
	
	protected void createField(int x, int y) {
		this.fields[x][y] = new Field(x, y);
	}
	
	@Override
	public Field getField(int x, int y) {
		return fields[x][y];
	}

	@Override
	public Field getField(Piece piece) {
		return pieces.inverse().get(piece);
	}

	@Override
	public Set<Field> getFields() {
		// "Listify" the field array column by column
		Set<Field> allFields = new HashSet<Field>(WIDTH * HEIGHT);
		for(Field[] column : fields) {
			allFields.addAll(Arrays.asList(column));
		}
		return allFields;
	}

	@Override
	public Field getFieldInDirection(Field origin, Direction dir) {
		ArgumentChecker.checkForNull(origin, dir);
		
		try {
			return this.getField(origin.getPosX() + dir.getX(), origin.getPosY() + dir.getY());
		} catch(ArrayIndexOutOfBoundsException exc) {
			return null;
		}
	}

	@Override
	public List<Field> getFieldsInDirection(Field start, Direction dir) {
		ArgumentChecker.checkForNull(start, dir);
		
		List<Field> directionFields = new ArrayList<Field>();
		// Iterate through all fields in the given direction (untill we hit null
		// which means we're off the board)
		Field currField = start;
		while((currField = this.getFieldInDirection(currField, dir)) != null) {
			directionFields.add(currField);
		}
		
		return directionFields;
	}

	@Override
	public Set<Piece> getPieces() {
		Set<Piece> allPieces = new HashSet<Piece>();
		
		for(Piece piece : pieces.values()) {
			allPieces.add(piece);
		}
		
		return allPieces;
	}

	@Override
	public Set<Piece> getPieces(Player player) {
		Set<Piece> playerPieces = new HashSet<Piece>();

		for(Piece piece : pieces.values()) {
			if(piece.getPlayer() == player) {
				playerPieces.add(piece);
			}
		}
		
		return playerPieces;
	}

	@Override
	public Piece getPiece(Field field) {
		return pieces.get(field);
	}

	@Override
	public void setPiece(Field field, Piece piece) {
		ArgumentChecker.checkForNull(field);
		pieces.put(field, piece);
	}

	@Override
	public Piece movePiece(Piece piece, Field target) {
		return pieces.put(target, piece);
	}

	@Override
	public Piece removePiece(Field field) {
		return pieces.remove(field);
	}
}
