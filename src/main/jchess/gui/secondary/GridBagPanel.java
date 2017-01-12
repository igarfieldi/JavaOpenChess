package jchess.gui.secondary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

public abstract class GridBagPanel extends JPanel
{
	private static final long serialVersionUID = 2558539019418926458L;
	
	private GridBagLayout gridBagLayout;
	protected GridBagConstraints gridBagConstraints;
	
	protected static final int LEFT = 0;
	protected static final int RIGHT = 1;

	public GridBagPanel()
	{
		initializeLayout();
		initializeGuiElements();
		placeGuiElements();
	}

	private void initializeLayout()
	{
		final Insets DEFAULT_INSETS = new Insets(3,3,3,3);
		
		gridBagLayout = new GridBagLayout();
		this.setLayout(this.gridBagLayout);
		
		gridBagConstraints = new GridBagConstraints();
		this.gridBagConstraints.insets = DEFAULT_INSETS;
	}
	
	protected abstract void initializeGuiElements();
	
	protected abstract void placeGuiElements();

	protected void setGridBagConstraints(JComponent guiElement, int gridx, int gridy)
	{
		this.gridBagConstraints.gridx = gridx;
		this.gridBagConstraints.gridy = gridy;
		this.gridBagLayout.setConstraints(guiElement, gridBagConstraints);
		this.add(guiElement);
	}
}