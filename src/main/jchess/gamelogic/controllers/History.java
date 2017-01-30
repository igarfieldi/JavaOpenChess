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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import jchess.gamelogic.Player;
import jchess.gamelogic.field.Field;
import jchess.gamelogic.field.Move;
import jchess.gamelogic.field.Move.CastlingType;
import jchess.gamelogic.pieces.King;
import jchess.gamelogic.pieces.Piece;
import jchess.gamelogic.pieces.PieceFactory.PieceType;
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
	private Map<Player, List<Move>> moveHistory;
	private Move lastMove;
	private IChessboardController chessboard;
	private List<Player> players;
	private int currentRowCount = 0;
	
	public History(IChessboardController chessboard, List<Player> players) {
		super();
		this.players = players;
		this.chessboard = chessboard;
		this.tableModel = new MyDefaultTableModel();
		this.table = new JTable(this.tableModel);
		this.view = new HistoryView(this.table, players.size());
		this.moveHistory = new HashMap<>();
		this.lastMove = null;
		//this.table.setMinimumSize(new Dimension(100, 100));
		
		for(Player player : players) {
			this.tableModel.addColumn(player.getColor().name());
			moveHistory.put(player, new ArrayList<Move>());
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
		int currentColumn = chessboard.getPlayerIndex(chessboard.getActivePlayer());
		if(currentColumn == 0) {
			this.addRow();
			this.currentRowCount++;
		}
		this.tableModel.setValueAt(str, currentRowCount - 1, currentColumn);
		
		this.table.scrollRectToVisible(table.getCellRect(table.getRowCount() - 1, 0, true));
	}

	@Override
	public void addMove(Move move)
	{
		ArgumentChecker.checkForNull(move);
		
		String moveString = move.toString();
		
		// Gotta enter the move to the history now so that en passant and whatnot
		// can detect it properly
		this.moveHistory.get(move.getMovedPiece().getPlayer()).add(move);
		this.lastMove = move;
		
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
	
	/**
	 * Clears the entire history.
	 */
	public void clearHistory() {
		for(Player player : players) {
			this.moveHistory.get(player).clear();
		}
		this.lastMove = null;
		this.move.clear();
		this.tableModel.setRowCount(0);
		this.currentRowCount = 0;
	}

	@Override
	public HistoryView getView()
	{
		return this.view;
	}
	
	@Override
	public Move getLastMove(Player player) {
		if(currentRowCount == 0) {
			return null;
		}
		
		List<Move> playerMoves = this.moveHistory.get(player);
		if(playerMoves == null || playerMoves.isEmpty()) {
			return null;
		} else {
			return playerMoves.get(playerMoves.size() - 1);
		}
	}
	
	@Override
	public Move getLastMove()
	{
		return lastMove;
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
	 * Splits the given game string into the individual moves.
	 * @param game Game string
	 * @return List of move strings
	 */
	private List<String> splitIntoMoves(String game) {
		List<String> moves = new ArrayList<>();
		for(String round : game.split("\\d\\. ")) {
			for(String move : round.split(" ")) {
				if(!move.isEmpty()) {
					moves.add(move);
				}
			}
		}
		
		return moves;
	}
	
	private Piece getPlayerKing(Player player) {
		for(Piece piece : chessboard.getBoard().getPieces(player)) {
			if(piece.getBehaviour() instanceof King) {
				return piece;
			}
		}
		
		return null;
	}
	
	/**
	 * Method to set all moves from String with validation test (useful for
	 * network game)
	 * 
	 * @param game
	 *            String to set in String like PGN with full-notation format
	 */
	@Override
	public void setMoves(String game)
	{
		for(String move : this.splitIntoMoves(game)) {
			Field from = null;
			Field to = null;
			if(move.equals("O-O")) {
				// Get the player's king
				Piece king = this.getPlayerKing(chessboard.getActivePlayer());
				// Get all possible castling moves
				for(Move castlingMove : chessboard.getCastleMoves(king)) {
					// Get the short castling one
					if(castlingMove.getCastlingMove() == CastlingType.SHORT_CASTLING) {
						from = castlingMove.getFrom();
						to = castlingMove.getTo();
						break;
					}
				}
			} else if(move.equals("O-O-O")) {
				// Get the player's king
				Piece king = this.getPlayerKing(chessboard.getActivePlayer());
				// Get all possible castling moves
				for(Move castlingMove : chessboard.getCastleMoves(king)) {
					// Get the long castling one
					if(castlingMove.getCastlingMove() == CastlingType.LONG_CASTLING) {
						from = castlingMove.getFrom();
						to = castlingMove.getTo();
						break;
					}
				}
			} else {
				String[] splitMove;
				
				if(move.contains("-")) {
					splitMove = move.split("-");
					// No capture
				} else {
					// Capture
					splitMove = move.split("x");
				}
				
				// First part is always field of origin
				String fromDesignation = splitMove[0];
				// Since there may be a symbol to designate the piece, remove it
				for(PieceType type : PieceType.values()) {
					if(type.getSymbol().equals(fromDesignation.substring(0, 1))) {
						fromDesignation = fromDesignation.substring(1);
						// Only one symbol should be removed!
						break;
					}
				}
				
				// For the to field we have to take care of some symbols
				// occurring after it
				String toDesignation = splitMove[1];
				if(toDesignation.contains("(e.p)")) {
					// En passant
					toDesignation = toDesignation.substring(0, toDesignation.indexOf('('));
				}
				if(toDesignation.contains("+")) {
					// Check
					toDesignation = toDesignation.substring(0, toDesignation.indexOf('+'));
				}
				if(toDesignation.contains("#")) {
					// Checkmate
					toDesignation = toDesignation.substring(0, toDesignation.indexOf('#'));
				}
				
				// Convert them to the fields
				from = Field.getFieldFromDesignation(fromDesignation);
				to = Field.getFieldFromDesignation(toDesignation);
			}
			
			// Carry out the move
			try {
				chessboard.move(from, to);
				chessboard.switchToNextPlayer();
			} catch(IllegalMoveException|NullPointerException exc) {
				log.log(Level.SEVERE, "Illegal move le loading!", exc);
				this.chessboard.getView().showMessage("illegal_move_on", move);
				this.chessboard.getView().unselect();
				return;// finish reading game and show message
			}
		}
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