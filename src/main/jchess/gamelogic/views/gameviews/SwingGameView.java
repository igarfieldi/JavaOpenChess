package jchess.gamelogic.views.gameviews;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jchess.Localization;
import jchess.gamelogic.views.IChessboardView;
import jchess.gamelogic.views.IGameView;
import jchess.gamelogic.views.IRenderable;
import jchess.gamelogic.views.chessboardviews.SquareChessboardView;

public class SwingGameView extends JPanel implements ComponentListener, IGameView
{
	private static final long serialVersionUID = -5137905954028032336L;
	
	private SquareChessboardView boardView;
	private JPanel infoPanel;
	
	public SwingGameView(IChessboardView view) {
		this.boardView = (SquareChessboardView) view;
		this.infoPanel = new JPanel();
		
		this.infoPanel.setLayout(new BoxLayout(this.infoPanel, BoxLayout.PAGE_AXIS));
		this.setLayout(new BorderLayout());
		
		super.add(this.boardView, BorderLayout.CENTER);
		super.add(this.infoPanel, BorderLayout.EAST);
		
		this.addComponentListener(this);
		this.setDoubleBuffered(true);
	}
	
	@Override
	public void changeSize(int width, int height) {
		this.setSize(width, height);
	}
	
	@Override
	public void addInfoComponent(IRenderable component) {
		this.infoPanel.add((Component) component);
	}
	
	@Override
	public IChessboardView getChessboardView() {
		return boardView;
	}

	@Override
	public void componentResized(ComponentEvent e)
	{
		// Figure out the width of the component's to the side
		// The height of the chessboard then gets adapted to remain squared
		this.boardView.changeSize(this.getWidth() - this.infoPanel.getWidth(), this.getHeight());
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
