package jchess.gamelogic.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import jchess.gamelogic.pieces.Piece;
import jchess.util.ArgumentChecker;
import jchess.util.Direction;

public class ChessboardModel
{
	private final int WIDTH;
	private final int HEIGHT;
	private Field[][] fields;
	private Map<Field, Piece> pieces;
	
	public void initialize() {
		fields = new Field[WIDTH][HEIGHT];
		pieces = new Hashtable<Field, Piece>();
		
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				fields[x][y] = new Field(x, y, null);
			}
		}
	}
	
	public ChessboardModel(int width, int height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		this.initialize();
	}
	
	public Field getField(int x, int y) {
		return fields[x][y];
	}
	
	public List<Field> getFields() {
		// "Listify" the field array column by column
		List<Field> allFields = new ArrayList<Field>(WIDTH * HEIGHT);
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
	
	public Piece getPiece(Field field) {
		return pieces.get(field);
	}
	
	public void setPiece(Field field, Piece piece) {
		ArgumentChecker.checkForNull(field);
		pieces.put(field, piece);
	}
	
	public Piece removePiece(Field field) {
		return pieces.remove(field);
	}
}
