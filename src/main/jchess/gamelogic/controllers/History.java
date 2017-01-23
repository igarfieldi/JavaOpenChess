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
package jchess.gamelogic.controllers;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import jchess.gamelogic.Player;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Move;
import jchess.gamelogic.field.Move.CastlingType;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.views.HistoryView;
import jchess.util.ArgumentChecker;

/**
 * Class representing the players moves, it's also checking that the moves taken
 * by player are correct. All moves which was taken by current player are saving
 * as List of Strings The history of moves is printing in a table.
 */
public class History extends AbstractTableModel implements IHistory
{
	private static final long serialVersionUID = -316401211821501289L;
	private static Logger log = Logger.getLogger(History.class.getName());
	
	private ArrayList<String> move = new ArrayList<String>();
	private MyDefaultTableModel tableModel;
	private HistoryView view;
	private JTable table;
	private Stack<Move> moveBackStack = new Stack<Move>();
	private Stack<Move> moveForwardStack = new Stack<Move>();
	private IChessboardController chessboard;
	private List<Player> players;
	private int currentRowCount = 0;
	private int currentPlayer = 0;
	
	public History(IChessboardController chessboard, List<Player> players) {
		super();
		this.players = players;
		this.chessboard = chessboard;
		this.tableModel = new MyDefaultTableModel();
		this.table = new JTable(this.tableModel);
		this.view = new HistoryView(this.table);
		//this.table.setMinimumSize(new Dimension(100, 100));
		
		for(Player player : players) {
			this.tableModel.addColumn(player.getColor().name());
		}
		this.addTableModelListener(null);
		this.tableModel.addTableModelListener(null);
	}
	
	@Override
	public String getValueAt(int x, int y)
	{
		return this.move.get((y * 2) - 1 + (x - 1));
	}
	
	@Override
	public int getRowCount()
	{
		return this.currentRowCount;
	}

	@Override
	public int getColumnCount()
	{
		return this.players.size();
	}
	
	protected void addRow()
	{
		this.tableModel.addRow(new String[2]);
	}
	
	@Override
	public boolean isCellEditable(int a, int b)
	{
		return false;
	}
	
	/**
	 * Method of adding new moves to the table
	 * 
	 * @param str
	 *            String which in is saved player move
	 */
	protected void addMove2Table(String str)
	{
		if(currentPlayer == 0) {
			this.addRow();
			this.currentRowCount++;
		}
		this.tableModel.setValueAt(str, currentRowCount - 1, currentPlayer);
		
		if(++currentPlayer >= this.players.size()) {
			currentPlayer = 0;
		}
		
		this.table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0, true));
	}
	
	public void addMove(Move move, boolean registerInHistory)
	{
		ArgumentChecker.checkForNull(move);
		
		String moveString = move.toString();
		
		// Gotta enter the move to the history now so that en passant and whatnot
		// can detect it properly
		if(registerInHistory)
		{
			this.moveBackStack.add(move);
		}
		
		// Temporarily switch to the next player so the check condition can be
		// verified
		chessboard.switchToNextPlayer();
		if(chessboard.isChecked(chessboard.getActivePlayer())) {
			
			if(chessboard.isCheckmated(chessboard.getActivePlayer())) {
				moveString += "#";
			} else {
				moveString += "+";
			}
			
		}
		// Switch back to the original player
		chessboard.switchToPreviousPlayer();
		
		this.move.add(moveString);
		this.addMove2Table(moveString);
		
		this.view.scrollRectToVisible(new Rectangle(0, this.view.getHeight() - 2, 1, 1));
	}
	
	public void clearMoveForwardStack()
	{
		this.moveForwardStack.clear();
	}
	
	public HistoryView getView()
	{
		return this.view;
	}
	
	public synchronized Move getLastMoveFromHistory()
	{
		try
		{
			Move last = this.moveBackStack.get(this.moveBackStack.size() - 1);
			return last;
		} catch(java.lang.ArrayIndexOutOfBoundsException exc)
		{
			return null;
		}
	}
	
	/**
	 * Method with is checking is the move is correct
	 * 
	 * @param move
	 *            String which in is capt player move
	 * @return boolean 1 if the move is correct, else 0
	 */
	static public boolean isMoveCorrect(String move)
	{
		if(move.equals("O-O") || move.equals("O-O-O"))
		{
			return true;
		}
		try
		{
			int from = 0;
			int sign = move.charAt(from);// get First
			switch(sign) // if sign of piece, get next
			{
				case 66: // B like Bishop
				case 75: // K like King
				case 78: // N like Knight
				case 81: // Q like Queen
				case 82:
					from = 1;
					break; // R like Rook
			}
			sign = move.charAt(from);
			log.log(Level.FINE, "Sign: " + sign);
			if(sign < 97 || sign > 104) // if lower than 'a' or higher than 'h'
			{
				return false;
			}
			sign = move.charAt(from + 1);
			if(sign < 49 || sign > 56) // if lower than '1' or higher than '8'
			{
				return false;
			}
			if(move.length() > 3) // if is equal to 3 or lower, than it's in
			                      // short notation, no more checking needed
			{
				sign = move.charAt(from + 2);
				if(sign != 45 && sign != 120) // if isn't '-' and 'x'
				{
					return false;
				}
				sign = move.charAt(from + 3);
				if(sign < 97 || sign > 104) // if lower than 'a' or higher than
				                            // 'h'
				{
					return false;
				}
				sign = move.charAt(from + 4);
				if(sign < 49 || sign > 56) // if lower than '1' or higher than
				                           // '8'
				{
					return false;
				}
			}
		} catch(java.lang.StringIndexOutOfBoundsException exc)
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * Method of getting the moves in string
	 * 
	 * @return str String which in is capt player move
	 */
	public String getMovesAsString()
	{
		int n = 1;
		int i = 0;
		String str = new String();
		for(String locMove : this.move)
		{
			if(i % players.size() == 0)
			{
				str += n + ". ";
				n += 1;
			}
			str += locMove + " ";
			i += 1;
		}
		return str;
	}
	
	/**
	 * Method to set all moves from String with validation test (useful for
	 * network game)
	 * 
	 * @param moves
	 *            String to set in String like PGN with full-notation format
	 */
	public void setMoves(String moves)
	{
		int from = 0;
		int to = 0;
		int n = 1;
		ArrayList<String> tempArray = new ArrayList<String>();
		int tempStrSize = moves.length() - 1;
		while(true)
		{
			from = moves.indexOf(" ", from);
			to = moves.indexOf(" ", from + 1);
			if(to < 0) {
				to = moves.length();
			}
			log.log(Level.FINE, from + ">" + to);
			try
			{
				tempArray.add(moves.substring(from + 1, to).trim());
			} catch(java.lang.StringIndexOutOfBoundsException exc)
			{
				log.log(Level.SEVERE, "Error while parsing moves from file!", exc);
				break;
			}
			if(n % players.size() == 0)
			{
				from = moves.indexOf(".", to);
				if(from < to)
				{
					break;
				}
			} else
			{
				from = to;
			}
			n += 1;
			if(from > tempStrSize || to > tempStrSize)
			{
				break;
			}
		}
		for(String locMove : tempArray)
		{
			System.out.println(locMove);
			if(locMove.equals("O-O-O") || locMove.equals("O-O")) // if castling
			{
				int[] values = new int[4];
				if(locMove.equals("O-O-O"))
				{
					if(this.chessboard.getActivePlayer().getColor() == Player.Color.BLACK) // if
					// black
					// turn
					{
						values = new int[]{ 4, 0, 2, 0 };// move value for
						                                 // castling (King move)
					} else
					{
						values = new int[]{ 4, 7, 2, 7 };// move value for
						                                 // castling (King move)
					}
				} else if(locMove.equals("O-O")) // if short castling
				{
					if(this.chessboard.getActivePlayer().getColor() == Player.Color.BLACK) // if
					// black
					// turn
					{
						values = new int[]{ 4, 0, 6, 0 };// move value for
						                                 // castling (King move)
					} else
					{
						values = new int[]{ 4, 7, 6, 7 };// move value for
						                                 // castling (King move)
					}
				}
				
				if(!this.isValidMove(values[0], values[1], values[2], values[3])) // if
				                                                                  // move
				                                                                  // is
				                                                                  // illegal
				{
					if(chessboard.getView() != null) {
						this.chessboard.getView().showMessage("illegal_move_on", locMove);
					}
					return;// finish reading game and show message
				}
				
				try {
					chessboard.move(new Field(values[0], values[1]), new Field(values[2], values[3]));
					chessboard.switchToNextPlayer();
				} catch(IllegalMoveException exc) {
					log.log(Level.SEVERE, "Illegal move le loading!", exc);
					if(chessboard.getView() != null) {
						this.chessboard.getView().showMessage("illegal_move_on", locMove);
						this.chessboard.getView().unselect();
					}
					return;// finish reading game and show message
				}
				continue;
			}
			from = 0;
			int num = locMove.charAt(from);
			if(num <= 90 && num >= 65)
			{
				from = 1;
			}
			int xFrom = 9; // set to higher value than chessboard has fields, to
			               // cause error if piece won't be found
			int yFrom = 9;
			int xTo = 9;
			int yTo = 9;
			boolean pieceFound = false;
			if(locMove.length() <= 3)
			{
				Field tempTo = Field.getFieldFromDesignation(locMove.substring(from, from + 1));
				xTo = tempTo.getPosX();
				yTo = tempTo.getPosY();
				
				for(Field field : chessboard.getBoard().getFields())
				{
					Piece piece = chessboard.getBoard().getPiece(field);
					if(piece == null || this.chessboard.getActivePlayer().getColor() != piece.getPlayer().getColor())
					{
						continue;
					}
					for(Move possibleMove : chessboard.getPossibleMoves(piece, true))
					{
						Field target = possibleMove.getTo();
						if(target.getPosX() == xTo && target.getPosY() == yTo)
						{
							xFrom = field.getPosX();
							yFrom = field.getPosY();
							pieceFound = true;
						}
					}
					if(pieceFound)
					{
						break;
					}
				}
			} else
			{
				
				Field tempFrom = Field.getFieldFromDesignation(locMove.substring(from, from + 2));
				Field tempTo = Field.getFieldFromDesignation(locMove.substring(from + 3, from + 5));
				
				xFrom = tempFrom.getPosX();
				yFrom = tempFrom.getPosY();
				xTo = tempTo.getPosX();
				yTo = tempTo.getPosY();
			}
			try {
				chessboard.move(new Field(xFrom, yFrom), new Field(xTo, yTo));
				chessboard.switchToNextPlayer();
			} catch(IllegalMoveException exc) {
				log.log(Level.SEVERE, "Illegal move le loading!", exc);
				this.chessboard.getView().showMessage("illegal_move_on", locMove);
				this.chessboard.getView().unselect();
				return;// finish reading game and show message
			}
		}
	}
	
	private boolean isValidMove(int xFrom, int yFrom, int xTo, int yTo)
	{
		Field from = chessboard.getBoard().getField(xFrom, yFrom);
		if(from != null)
		{
			Field to = chessboard.getBoard().getField(xTo, yTo);
			Piece piece = chessboard.getBoard().getPiece(from);
			if(to != null && piece != null)
			{
				for(Move move : chessboard.getPossibleMoves(piece, true)) {
					if(move.getTo().equals(to)) {
						return true;
					}
				}
				
				return false;
			}
		}
		
		return false;
	}
}

/*
 * Overriding DefaultTableModel and isCellEditable method (history cannot be
 * edited by player)
 */

class MyDefaultTableModel extends DefaultTableModel
{
	private static final long serialVersionUID = -3929714645844464804L;
	
	MyDefaultTableModel()
	{
		super();
	}
	
	@Override
	public boolean isCellEditable(int a, int b)
	{
		return false;
	}
}