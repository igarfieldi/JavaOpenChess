package jchess.gui.secondary;

import java.awt.Window;

import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;

import jchess.JChessApp;
import jchess.gui.setup.GridBagPanel;
import jchess.util.TypedResourceBundle;

public class AboutPanel extends GridBagPanel
{
	private static final long serialVersionUID = 1265012929536332512L;
	
	private static final TypedResourceBundle ABOUT_PROPERTIES = new TypedResourceBundle(
	        "jchess.resources.JChessAboutBox");
	
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
		imageLabel.setName("imageLabel");
	}
	
	private void initializeCloseButton()
	{
		closeButton = new JButton();
		actionMap = Application.getInstance(JChessApp.class).getContext().getActionMap(AboutPanel.class, this);
		
		closeButton.setAction(actionMap.get("closeAboutBox"));
		closeButton.setName("closeButton");
	}
	
	@Action
	public void closeAboutBox()
	{
		Window parentDialog = SwingUtilities.getWindowAncestor(this);
		parentDialog.dispose();
	}
	
	protected void placeGuiElements()
	{
		setGridBagConstraints(imageLabel, 0, 0);
		setGridBagConstraints(applicationInfoPanel, 1, 0);
		setGridBagConstraints(closeButton, 1, 1);
	}
}
