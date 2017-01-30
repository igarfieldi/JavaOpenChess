package jchess.gamelogic.views;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;

public class HistoryView extends JScrollPane implements IRenderable
{
	private static final long serialVersionUID = -987353227190945465L;
	private static final int SIZE_PER_PLAYER = 90;

	public HistoryView(JTable table, int playerCount) {
		super(table);
		this.setMinimumSize(new Dimension(SIZE_PER_PLAYER*playerCount, 250));
		// Have the history take up all the available space; maybe not so smart?
		this.setPreferredSize(new Dimension(SIZE_PER_PLAYER*playerCount, 50000));
		this.setAutoscrolls(true);
	}
	
	@Override
	public void render() {
		this.repaint();
	}
	
	@Override
	public void changeSize(int width, int height) {
		this.setSize(width, height);
	}
}
