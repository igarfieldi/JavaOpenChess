package jchess.gui.secondary.about;

import java.awt.Window;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;

import jchess.JChessApp;
import jchess.gui.secondary.GridBagPanel;

public class AboutPanel extends GridBagPanel implements IResources
{
	private static final long serialVersionUID = 1265012929536332512L;
	
	private ApplicationInfoPanel applicationInfoPanel;
	private JLabel imageLabel;
	private JButton closeButton;
	
	ActionMap actionMap;
	
	protected void initializeGuiElements()
	{
		applicationInfoPanel = new ApplicationInfoPanel();
		
		initializeImageLabel();
		initializeCloseButton();
	}
	
	private void initializeImageLabel()
	{
		imageLabel = new JLabel();
		imageLabel.setIcon(ABOUT_PROPERTIES.getIcon("imageLabel.icon"));
	}
	
	private void initializeCloseButton()
	{
		closeButton = new JButton();
		actionMap = Application.getInstance(JChessApp.class).getContext().getActionMap(AboutPanel.class, this);
		closeButton.setAction(actionMap.get("closeAboutBox"));
	}
	
	@Action
	public void closeAboutBox()
	{
		Window parentDialog = SwingUtilities.getWindowAncestor(this);
		parentDialog.dispose();
	}
	
	protected void placeGuiElements()
	{
		setGridBagConstraints(imageLabel, LEFT, 0);
		setGridBagConstraints(applicationInfoPanel, RIGHT, 0);
		setGridBagConstraints(closeButton, RIGHT, 1);
	}
}
