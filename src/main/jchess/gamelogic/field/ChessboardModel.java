package jchess.gamelogic.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import jchess.gamelogic.Player;
import jchess.gamelogic.pieces.Piece;
import jchess.util.ArgumentChecker;
import jchess.util.BiMap;
import jchess.util.Direction;

public class ChessboardModel implements IChessboardModel
{
	private final int WIDTH;
	private final int HEIGHT;
	private Field[][] fields;
	private BiMap<Field, Piece> pieces;
	
	public void initialize() {
		fields = new Field[WIDTH][HEIGHT];
		pieces = new BiMap<Field, Piece>();
		
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				fields[x][y] = new Field(x, y);
			}
		}
	}
	
	public ChessboardModel(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.initialize();
	}
	
	@Override
	public ChessboardModel copy() {
		ChessboardModel board = new ChessboardModel(WIDTH, HEIGHT);
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				board.fields[x][y] = new Field(fields[x][y]);
			}
		}
		
		for(Map.Entry<Field, Piece> entry : pieces.entrySet()) {
			board.setPiece(entry.getKey(), entry.getValue().copy());
		}
		
		return board;
	}
	
	public Field getField(int x, int y) {
		return fields[x][y];
	}
	
	public Field getField(Piece piece) {
		return pieces.inverse().get(piece);
	}
	
	public Set<Field> getFields() {
		// "Listify" the field array column by column
		Set<Field> allFields = new HashSet<Field>(WIDTH * HEIGHT);
		for(Field[] column : fields) {
			allFields.addAll(Arrays.asList(column));
		}
		return allFields;
	}
	
	public Field getFieldInDirection(Field origin, Direction dir) {
		ArgumentChecker.checkForNull(origin, dir);
		
		try {
			return this.getField(origin.getPosX() + dir.getX(), origin.getPosY() + dir.getY());
		} catch(ArrayIndexOutOfBoundsException exc) {
			return null;
		}
	}
	
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
	
	public Set<Piece> getPieces() {
		Set<Piece> allPieces = new HashSet<Piece>();
		
		for(Piece piece : pieces.values()) {
			allPieces.add(piece);
		}
		
		return allPieces;
	}
	
	public Set<Piece> getPieces(Player player) {
		Set<Piece> playerPieces = new HashSet<Piece>();

		for(Piece piece : pieces.values()) {
			if(piece.getPlayer() == player) {
				playerPieces.add(piece);
			}
		}
		
		return playerPieces;
	}
	
	public Piece getPiece(Field field) {
		return pieces.get(field);
	}
	
	public void setPiece(Field field, Piece piece) {
		ArgumentChecker.checkForNull(field);
		pieces.put(field, piece);
	}
	
	public Piece movePiece(Piece piece, Field target) {
		return pieces.put(target, piece);
	}
	
	public Piece removePiece(Field field) {
		return pieces.remove(field);
	}
}
