package jchess.gamelogic.views;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jchess.Localization;

public class GameView extends JPanel implements ComponentListener, IGameView
{
	private static final long serialVersionUID = -5137905954028032336L;
	
	private ChessboardView boardView;
	private GameClockView clockView;
	private JScrollPane historyView;
	
	public GameView(ChessboardView boardView, GameClockView clockView, JScrollPane historyView) {
		this.boardView = boardView;
		this.clockView = clockView;
		this.historyView = historyView;

		this.boardView.setSize(ChessboardView.IMG_HEIGHT, ChessboardView.IMG_WIDTH);
		this.boardView.setLocation(new Point(0, 0));
		this.boardView.setVisible(true); // TODO: necessary?
		this.add(this.boardView);
		
		this.clockView.setSize(new Dimension(200, 100));
		this.clockView.setLocation(new Point(500, 0));
		this.clockView.setVisible(true); // TODO: necessary?
		this.add(this.clockView);
		
		// TODO: replace with HistoryView class/interface
		this.historyView.setSize(new Dimension(180, 350));
		this.historyView.setLocation(new Point(500, 121));
		this.historyView.setVisible(true); // TODO: necessary?
		this.add(this.historyView);

		// General layout stuff
		this.setLayout(null);
		this.addComponentListener(this);
		this.setDoubleBuffered(true);
	}
	
	public IChessboardView getChessboardView() {
		return boardView;
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		// Compute new height for board (has to stay square)
		int height = this.getHeight() >= this.getWidth() ? this.getWidth() : this.getHeight();
		int chess_height = (int) Math.round((height * 0.8) / 8) * 8;
		this.boardView.resizeChessboard(chess_height);
		
		// Reposition history and clock views to match new board height
		chess_height = this.boardView.getHeight();
		this.historyView.setLocation(new Point(chess_height + 5, 100));
		this.historyView.setSize(this.historyView.getWidth(), chess_height - 100);
		this.clockView.setLocation(new Point(chess_height + 5, 0));
	}

	public void render() {
		this.repaint();
	}
	
	public void showMessage(String key) {
		JOptionPane.showMessageDialog(this, Localization.getMessage(key));
	}
	
	public void showMessage(String key, String args) {
		JOptionPane.showMessageDialog(this, Localization.getMessage(key) + ": " + args);
	}
	
	public boolean showYesNoDialog(String key) {
		return JOptionPane.showConfirmDialog(this, Localization.getMessage(key),
				Localization.getMessage(key), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
	}

	@Override
	public void componentMoved(ComponentEvent e)
	{
	}

	@Override
	public void componentShown(ComponentEvent e)
	{
	}

	@Override
	public void componentHidden(ComponentEvent e)
	{
	}
}
