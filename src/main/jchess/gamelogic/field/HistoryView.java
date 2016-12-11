package jchess.gamelogic.field;

import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTable;

import jchess.gamelogic.views.IRenderable;

public class HistoryView extends JScrollPane implements IRenderable
{
	private static final long serialVersionUID = -987353227190945465L;

	public HistoryView(JTable table) {
		super(table);
		this.setMinimumSize(new Dimension(180, 250));
		// Have the history take up all the available space; maybe not so smart?
		this.setPreferredSize(new Dimension(180, 50000));
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
