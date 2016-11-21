package jchess.gamelogic.field;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import jchess.gamelogic.pieces.Piece;

public class ChessboardModel
{
	private static final int WIDTH = 8;
	private static final int HEIGHT = 8;
	
	private Field[][] fields = new Field[WIDTH][HEIGHT];
	private Map<Field, Piece> pieces = new Hashtable<Field, Piece>();
	
	public ChessboardModel() {
		for(int x = 0; x < WIDTH; x++) {
			for(int y = 0; y < HEIGHT; y++) {
				fields[x][y] = new Field(x, y, null);
			}
		}
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
	
	public Piece getPiece(Field field) {
		return pieces.get(field);
	}
	
	public void setPiece(Field field, Piece piece) {
		pieces.put(field, piece);
	}
	
	public Piece removePiece(Field field) {
		return pieces.remove(field);
	}
}
