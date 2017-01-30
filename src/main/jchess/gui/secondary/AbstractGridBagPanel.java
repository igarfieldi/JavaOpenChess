package jchess.gui.secondary;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * This class is used for building panels with a GridBagLayout.
 * With such a layout, UI elements are placed in a grid.
 * Any class that extends from this will use that layout.
 */
public abstract class AbstractGridBagPanel extends JPanel
{
	private static final long serialVersionUID = 2558539019418926458L;
	
	private GridBagLayout gridBagLayout;
	protected GridBagConstraints gridBagConstraints;
	
	protected static final int LEFT = 0;
	protected static final int MIDDLE = 1;
	protected static final int RIGHT = 2;

	public AbstractGridBagPanel()
	{
		super();
		initializeLayout();
		initializeGuiElements();
		placeGuiElements();
	}

	/**
	 * Initializes the GridBagLayout and its constraints
	 */
	private void initializeLayout()
	{
		final Insets DEFAULT_INSETS = new Insets(3,3,3,3);
		
		gridBagLayout = new GridBagLayout();
		this.setLayout(this.gridBagLayout);
		
		gridBagConstraints = new GridBagConstraints();
		this.gridBagConstraints.insets = DEFAULT_INSETS;
	}
	
	/**
	 * This method is used to instantiate instances of UI element variables.
	 */
	protected abstract void initializeGuiElements();
	
	/**
	 * This method is used to place all UI elements with setGridBagConstraints.
	 */
	protected abstract void placeGuiElements();

	/**
	 * Places a UI element onto a specified grid cell.
	 * 
	 * @param guiElement
	 * 				UI element to be placed.
	 * @param gridx
	 * 				x-index of the grid cell.
	 * @param gridy
	 * 				y-index of th grid cell.
	 */
	protected void setGridBagConstraints(JComponent guiElement, int gridx, int gridy)
	{
		this.gridBagConstraints.gridx = gridx;
		this.gridBagConstraints.gridy = gridy;
		this.gridBagLayout.setConstraints(guiElement, gridBagConstraints);
		this.add(guiElement);
	}
}