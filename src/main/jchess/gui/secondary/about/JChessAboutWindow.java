package jchess.gui.secondary.about;

import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

/**
 * Class with a dialog containing the about panel.
 */
public class JChessAboutWindow extends JDialog implements IResources
{
	private static final long serialVersionUID = 8209938830184516667L;
	
	public JChessAboutWindow(Frame parent)
	{
		super(parent);
		setWindowProperties();
		
		this.add(new AboutPanel());
	}
	
	private void setWindowProperties()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(ABOUT_PROPERTIES.getString("title"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		pack();
		
		setResizable(false);
	}
}
