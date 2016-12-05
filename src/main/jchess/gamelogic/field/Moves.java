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
package jchess.gamelogic.field;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import jchess.Localization;
import jchess.gamelogic.Player;
import jchess.gamelogic.Settings;
import jchess.gamelogic.controllers.IChessboardController;
import jchess.gamelogic.field.Move.CastlingType;
import jchess.gamelogic.pieces.Piece;

/**
 * Class representing the players moves, it's also checking that the moves taken
 * by player are correct. All moves which was taken by current player are saving
 * as List of Strings The history of moves is printing in a table.
 */
public class Moves extends AbstractTableModel
{
	private static final long serialVersionUID = -316401211821501289L;
	private static Logger log = Logger.getLogger(Moves.class.getName());
	
	private ArrayList<String> move = new ArrayList<String>();
	private int columnsNum = 3;
	private int rowsNum = 0;
	private String[] names = new String[]{ Localization.getMessage("white"), Localization.getMessage("black") };
	private MyDefaultTableModel tableModel;
	private JScrollPane scrollPane;
	private JTable table;
	private boolean enterBlack = false;
	private Stack<Move> moveBackStack = new Stack<Move>();
	private Stack<Move> moveForwardStack = new Stack<Move>();
	private IChessboardController chessboard;
	private Settings settings;
	
	public Moves(IChessboardController chessboard, Settings settings)
	{
		super();
		this.settings = settings;
		this.chessboard = chessboard;
		this.tableModel = new MyDefaultTableModel();
		this.table = new JTable(this.tableModel);
		this.scrollPane = new JScrollPane(this.table);
		this.scrollPane.setMaximumSize(new Dimension(100, 100));
		this.table.setMinimumSize(new Dimension(100, 100));
		
		this.tableModel.addColumn(this.names[0]);
		this.tableModel.addColumn(this.names[1]);
		this.addTableModelListener(null);
		this.tableModel.addTableModelListener(null);
		this.scrollPane.setAutoscrolls(true);
	}
	
	public void draw()
	{
	}
	
	@Override
	public String getValueAt(int x, int y)
	{
		return this.move.get((y * 2) - 1 + (x - 1));
	}
	
	@Override
	public int getRowCount()
	{
		return this.rowsNum;
	}
	
	@Override
	public int getColumnCount()
	{
		return this.columnsNum;
	}
	
	protected void addRow()
	{
		this.tableModel.addRow(new String[2]);
	}
	
	protected void addCastling(String move)
	{
		this.move.remove(this.move.size() - 1);// remove last element (move of
		                                       // Rook)
		if(!this.enterBlack)
		{
			this.tableModel.setValueAt(move, this.tableModel.getRowCount() - 1, 1);// replace
			                                                                       // last
			                                                                       // value
		} else
		{
			this.tableModel.setValueAt(move, this.tableModel.getRowCount() - 1, 0);// replace
			                                                                       // last
			                                                                       // value
		}
		this.move.add(move);// add new move (O-O or O-O-O)
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
		try
		{
			if(!this.enterBlack)
			{
				this.addRow();
				this.rowsNum = this.tableModel.getRowCount() - 1;
				this.tableModel.setValueAt(str, rowsNum, 0);
			} else
			{
				this.tableModel.setValueAt(str, rowsNum, 1);
				this.rowsNum = this.tableModel.getRowCount() - 1;
			}
			this.enterBlack = !this.enterBlack;
			this.table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0, true));// scroll
			                                                                                    // to
			                                                                                    // down
			
		} catch(java.lang.ArrayIndexOutOfBoundsException exc)
		{
			if(this.rowsNum > 0)
			{
				this.rowsNum--;
				addMove2Table(str);
			}
		}
	}
	
	/**
	 * Method of adding new move
	 * 
	 * @param move
	 *            String which in is capt player move
	 */
	public void addMove(String move)
	{
		if(isMoveCorrect(move))
		{
			this.move.add(move);
			this.addMove2Table(move);
			this.moveForwardStack.clear();
		}
		
	}
	
	public void addMove(Move move, boolean registerInHistory)
	{
		String locMove = new String(move.getMovedPiece().getSymbol());
		
		// Get field designation of 'from' field
		locMove += move.getFrom().toString();
		
		if(move.getTakenPiece() != null)
		{
			locMove += "x";// take down opponent piece
		} else
		{
			locMove += "-";// normal move
		}
		
		// Get field designation of 'from' field
		locMove += move.getTo().toString();
		
		if(move.wasEnPassant())
		{
			locMove += "(e.p)";// pawn take down opponent en passant
		}
		if((!this.enterBlack && this.chessboard.isChecked(settings.getBlackPlayer()))
		        || (this.enterBlack && this.chessboard.isChecked(settings.getWhitePlayer())))
		{// if checked
			
			if((!this.enterBlack && this.chessboard.isCheckmated(settings.getBlackPlayer()))
			        || (this.enterBlack && this.chessboard.isCheckmated(settings.getWhitePlayer())))
			{// check if checkmated
				locMove += "#";// check mate
			} else
			{
				locMove += "+";// check
			}
		}
		if(move.getCastlingMove() == CastlingType.SHORT_CASTLING)
		{
			this.addCastling("0-0");
		} else if(move.getCastlingMove() == CastlingType.LONG_CASTLING)
		{
			this.addCastling("0-0-0");
		} else
		{
			this.move.add(locMove);
			this.addMove2Table(locMove);
		}
		this.scrollPane.scrollRectToVisible(new Rectangle(0, this.scrollPane.getHeight() - 2, 1, 1));
		
		if(registerInHistory)
		{
			this.moveBackStack.add(move);
		}
	}
	
	public void clearMoveForwardStack()
	{
		this.moveForwardStack.clear();
	}
	
	public JScrollPane getScrollPane()
	{
		return this.scrollPane;
	}
	
	public ArrayList<String> getMoveList()
	{
		return this.move;
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
	
	public synchronized Move getNextMoveFromHistory()
	{
		try
		{
			Move next = this.moveForwardStack.get(this.moveForwardStack.size() - 1);
			return next;
		} catch(java.lang.ArrayIndexOutOfBoundsException exc)
		{
			return null;
		}
		
	}
	
	public synchronized Move undo()
	{
		try
		{
			Move last = this.moveBackStack.pop();
			if(last != null)
			{
				this.moveForwardStack.push(last);
				
				if(this.enterBlack)
				{
					this.tableModel.setValueAt("", this.tableModel.getRowCount() - 1, 0);
					this.tableModel.removeRow(this.tableModel.getRowCount() - 1);
					
					if(this.rowsNum > 0)
					{
						this.rowsNum--;
					}
				} else
				{
					if(this.tableModel.getRowCount() > 0)
					{
						this.tableModel.setValueAt("", this.tableModel.getRowCount() - 1, 1);
					}
				}
				this.move.remove(this.move.size() - 1);
				this.enterBlack = !this.enterBlack;
			}
			return last;
		} catch(java.util.EmptyStackException exc)
		{
			this.enterBlack = false;
			return null;
		} catch(java.lang.ArrayIndexOutOfBoundsException exc)
		{
			return null;
		}
	}
	
	public synchronized Move redo()
	{
		try
		{
			Move first = this.moveForwardStack.pop();
			this.moveBackStack.push(first);
			
			return first;
		} catch(java.util.EmptyStackException exc)
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
	
	public void addMoves(ArrayList<String> list)
	{
		for(String singleMove : list)
		{
			if(isMoveCorrect(singleMove))
			{
				this.addMove(singleMove);
			}
		}
	}
	
	/**
	 * Method of getting the moves in string
	 * 
	 * @return str String which in is capt player move
	 */
	public String getMovesInString()
	{
		int n = 1;
		int i = 0;
		String str = new String();
		for(String locMove : this.getMoveList())
		{
			if(i % 2 == 0)
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
	 * Method to set all moves from String with validation test (usefoul for
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
			log.log(Level.FINE, from + ">" + to);
			try
			{
				tempArray.add(moves.substring(from + 1, to).trim());
			} catch(java.lang.StringIndexOutOfBoundsException exc)
			{
				log.log(Level.SEVERE, "Error while parsing moves from file!", exc);
				break;
			}
			if(n % 2 == 0)
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
		for(String locMove : tempArray) // test if moves are written correctly
		{
			if(!Moves.isMoveCorrect(locMove.trim())) // if not
			{
				this.chessboard.getView().showMessage("invalid_file_to_load", move.toString());
				return;// show message and finish reading game
			}
		}
		for(String locMove : tempArray)
		{
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
					this.chessboard.getView().showMessage("illegal_move_on", locMove);
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
					for(Field possibleMove : chessboard.getPossibleMoves(piece, true))
					{
						if(possibleMove.getPosX() == xTo && possibleMove.getPosY() == yTo)
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
				Field tempFrom = Field.getFieldFromDesignation(locMove.substring(from, from + 1));
				Field tempTo = Field.getFieldFromDesignation(locMove.substring(from + 3, from + 4));
				
				xFrom = tempFrom.getPosX();
				yFrom = tempFrom.getPosY();
				xTo = tempTo.getPosX();
				yTo = tempTo.getPosY();
			}
			if(!this.isValidMove(xFrom, yFrom, xTo, yTo)) // if move is illegal
			{
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
				return chessboard.getPossibleMoves(piece, true).contains(to);
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