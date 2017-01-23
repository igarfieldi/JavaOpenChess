package jchess.gui.secondary.about;

import java.awt.Font;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import jchess.gui.secondary.AbstractGridBagPanel;
import jchess.util.TypedResourceBundle;

/**
 * This class is a panel that contains all information about the application.
 * This includes the title and version number of the application and the homepage and name of the developers.
 */
public class ApplicationInfoPanel extends AbstractGridBagPanel implements IResources
{
	private static final long serialVersionUID = 5974507268451667934L;
	
	private static final TypedResourceBundle APP_PROPERTIES = new TypedResourceBundle("jchess.resources.JChessApp");
	
	private JLabel appTitleLabel;
	private JLabel versionTitelLabel;
	private JLabel versionLabel;
	private JLabel homepageTitelLabel;
	private JLabel homepageLabel;
	private JLabel appDescriptionLabel;
	private JLabel activeDeveloperTitleLabel;
	private JLabel activeDeveloperLabel;
	private JLabel inactiveDevelopersTitleLabelLabel;
	private JLabel inactiveDevelopersLabel;
	
	public ApplicationInfoPanel()
	{
		super();
		initializeLabels();
	}
	
	private void initializeLabels()
	{
		final int NORMAL_SIZE = 0;
		final int BIG_SIZE = 4;
		
		setLabelContent(appTitleLabel, true, BIG_SIZE, "Application.title", APP_PROPERTIES);
		setLabelContent(appDescriptionLabel, false, NORMAL_SIZE, "appDescLabel.text", ABOUT_PROPERTIES);
		
		setLabelContent(versionTitelLabel, true, NORMAL_SIZE, "versionLabel.text", ABOUT_PROPERTIES);
		setLabelContent(versionLabel, false, NORMAL_SIZE, "Application.version", APP_PROPERTIES);
		
		setLabelContent(homepageTitelLabel, true, NORMAL_SIZE, "homepageLabel.text", ABOUT_PROPERTIES);
		setLabelContent(homepageLabel, false, NORMAL_SIZE, "Application.homepage", APP_PROPERTIES);
		
		setLabelContent(activeDeveloperTitleLabel, true, NORMAL_SIZE, "vendorLabel1.text", ABOUT_PROPERTIES);
		setLabelContent(activeDeveloperLabel, false, NORMAL_SIZE, "appHomepageLabel2.text", ABOUT_PROPERTIES);
		
		setLabelContent(inactiveDevelopersTitleLabelLabel, true, NORMAL_SIZE, "vendorLabel2.text", ABOUT_PROPERTIES);
		setLabelContent(inactiveDevelopersLabel, false, NORMAL_SIZE, "appHomepageLabel3.text", ABOUT_PROPERTIES);
	}
	
	/**
	 * Sets the text content inside the specified label as well as its font.
	 * 
	 * @param label
	 * 				The label to be used.
	 * @param hasBoldFont
	 * 				Specifies if the font is normal or bold
	 * @param fontEnlargement
	 * 				Specifies the text size
	 * @param resourceName
	 * 				The asset that contains the text.
	 * @param resourceBundle
	 * 				The asset pack that contains the texts.
	 */
	private void setLabelContent(JLabel label, Boolean hasBoldFont, int fontEnlargement, String resourceName,
	        TypedResourceBundle resourceBundle)
	{
		if(hasBoldFont)
		{
			Font labelFont = label.getFont();
			label.setFont(
			        labelFont.deriveFont(labelFont.getStyle() | Font.BOLD, labelFont.getSize() + fontEnlargement));
		}
		
		label.setText(resourceBundle.getString(resourceName));
	}
	
	protected void initializeGuiElements()
	{
		appTitleLabel = new JLabel();
		versionTitelLabel = new JLabel();
		versionLabel = new JLabel();
		homepageTitelLabel = new JLabel();
		homepageLabel = new JLabel();
		appDescriptionLabel = new JLabel();
		activeDeveloperTitleLabel = new JLabel();
		activeDeveloperLabel = new JLabel();
		inactiveDevelopersTitleLabelLabel = new JLabel();
		inactiveDevelopersLabel = new JLabel();
	}
	
	protected void placeGuiElements()
	{
		gridBagConstraints.anchor = GridBagConstraints.WEST;
		
		setGridBagConstraints(appTitleLabel, LEFT, 0);
		setGridBagConstraints(appDescriptionLabel, LEFT, 1);
		
		setGridBagConstraints(versionTitelLabel, LEFT, 2);
		setGridBagConstraints(versionLabel, RIGHT, 2);
		
		setGridBagConstraints(homepageTitelLabel, LEFT, 3);
		setGridBagConstraints(homepageLabel, RIGHT, 3);
		
		setGridBagConstraints(activeDeveloperTitleLabel, LEFT, 4);
		setGridBagConstraints(activeDeveloperLabel, RIGHT, 4);
		
		setGridBagConstraints(inactiveDevelopersTitleLabelLabel, LEFT, 5);
		setGridBagConstraints(inactiveDevelopersLabel, RIGHT, 5);
	}
}
