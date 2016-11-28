package jchess.gui.setup;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class GridBagPanel extends JPanel
{
	private static final long serialVersionUID = 2558539019418926458L;
	
	private GridBagLayout gridBagLayout;
	private GridBagConstraints gridBagConstraints;

	public GridBagPanel()
	{
		initializeLayout();
	}

	private void initializeLayout()
	{
		gridBagLayout = new GridBagLayout();
		this.setLayout(this.gridBagLayout);
		gridBagConstraints = new GridBagConstraints();
		this.gridBagConstraints.insets = new Insets(3, 3, 3, 3);
	}

	protected void setGridBagConstraints(JComponent guiElement, int gridx, int gridy)
	{
		this.gridBagConstraints.gridx = gridx;
		this.gridBagConstraints.gridy = gridy;
		this.gridBagLayout.setConstraints(guiElement, gridBagConstraints);
		this.add(guiElement);
	}
	
}