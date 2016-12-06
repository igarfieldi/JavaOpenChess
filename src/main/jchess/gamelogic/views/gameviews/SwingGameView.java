package jchess.gamelogic.views.gameviews;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jchess.Localization;
import jchess.gamelogic.views.GameClockView;
import jchess.gamelogic.views.IChessboardView;
import jchess.gamelogic.views.IGameView;
import jchess.gamelogic.views.chessboardviews.SquareChessboardView;

public class SwingGameView extends JPanel implements ComponentListener, IGameView
{
	private static final long serialVersionUID = -5137905954028032336L;
	
	private SquareChessboardView boardView;
	private GameClockView clockView;
	private JScrollPane historyView;
	
	public SwingGameView(GameClockView clockView, JScrollPane historyView) {
		this.clockView = clockView;
		this.historyView = historyView;

		this.add(this.clockView);
		this.clockView.setSize(new Dimension(200, 100));
		this.clockView.setLocation(new Point(500, 0));
		this.clockView.setVisible(true); // TODO: necessary?

		// TODO: replace with HistoryView class/interface
		this.add(this.historyView);
		this.historyView.setSize(new Dimension(180, 350));
		this.historyView.setLocation(new Point(500, 121));
		this.historyView.setVisible(true); // TODO: necessary?

		// General layout stuff
		this.setLayout(null);
		this.addComponentListener(this);
		this.setDoubleBuffered(true);
	}
	
	@Override
	public IChessboardView getChessboardView() {
		return boardView;
	}
	
	@Override
	public void setChessboardView(IChessboardView view) {
		this.boardView = (SquareChessboardView) view;
		this.add(this.boardView);
		this.boardView.setLocation(new Point(0, 0));
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
	
	@Override
	public void showMessage(String key, String arg) {
		String message = Localization.getMessage(key);
		if(!arg.isEmpty()) {
			message += " : " + arg;
		}
		JOptionPane.showMessageDialog(this, message);
	}
	
	@Override
	public Option showConfirmMessage(String key, String arg) {
		String message = Localization.getMessage(key);
		if(!arg.isEmpty()) {
			message += " : " + arg;
		}
		
		int selectedOption = JOptionPane.showConfirmDialog(this, message, "", JOptionPane.YES_NO_CANCEL_OPTION);
		
		if(selectedOption == JOptionPane.YES_OPTION) {
			return Option.YES;
		} else if(selectedOption == JOptionPane.NO_OPTION) {
			return Option.NO;
		} else {
			return Option.CANCEL;
		}
	}

	@Override
	public String showInputMessage(String key, String arg, String initialValue) {
		String message = Localization.getMessage(key);
		if(!arg.isEmpty()) {
			message += " : " + arg;
		}
		return JOptionPane.showInputDialog(this, message, initialValue);
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
