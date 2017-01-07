package jchess.gui.secondary;

import java.awt.Font;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import jchess.gui.GridBagPanel;
import jchess.util.TypedResourceBundle;

/**
 * 
 * This class is a panel that contains all information about the application.
 * This includes the title and version number of the application and the homepage and name of the developers.
 *
 */
public class ApplicationInfoPanel extends GridBagPanel
{
	private static final long serialVersionUID = 5974507268451667934L;
	
	private static final TypedResourceBundle ABOUT_PROPERTIES = new TypedResourceBundle(
	        "jchess.resources.JChessAboutBox");
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
		
		setLabelContent(appTitleLabel, true, BIG_SIZE, "Application.title", "Application.title", APP_PROPERTIES);
		setLabelContent(appDescriptionLabel, false, NORMAL_SIZE, "appDescLabel.text", "appDescLabel", ABOUT_PROPERTIES);
		
		setLabelContent(versionTitelLabel, true, NORMAL_SIZE, "versionLabel.text", "versionLabel", ABOUT_PROPERTIES);
		setLabelContent(versionLabel, false, NORMAL_SIZE, "Application.version", "appVersionLabel", APP_PROPERTIES);
		
		setLabelContent(homepageTitelLabel, true, NORMAL_SIZE, "homepageLabel.text", "homepageLabel", ABOUT_PROPERTIES);
		setLabelContent(homepageLabel, false, NORMAL_SIZE, "Application.homepage", "appHomepageLabel", APP_PROPERTIES);
		
		setLabelContent(activeDeveloperTitleLabel, true, NORMAL_SIZE, "vendorLabel1.text", "vendorLabel1",
		        ABOUT_PROPERTIES);
		setLabelContent(activeDeveloperLabel, false, NORMAL_SIZE, "appHomepageLabel2.text", "appHomepageLabel2",
		        ABOUT_PROPERTIES);
		
		setLabelContent(inactiveDevelopersTitleLabelLabel, true, NORMAL_SIZE, "vendorLabel2.text", "vendorLabel2",
		        ABOUT_PROPERTIES);
		setLabelContent(inactiveDevelopersLabel, false, NORMAL_SIZE, "appHomepageLabel3.text", "appHomepageLabel3",
		        ABOUT_PROPERTIES);
	}
	
	private void setLabelContent(JLabel label, Boolean hasBoldFont, int fontEnlargement, String resourceName,
	        String labelName, TypedResourceBundle resourceBundle)
	{
		if(hasBoldFont)
		{
			Font labelFont = label.getFont();
			label.setFont(
			        labelFont.deriveFont(labelFont.getStyle() | Font.BOLD, labelFont.getSize() + fontEnlargement));
		}
		
		label.setText(resourceBundle.getString(resourceName));
		label.setName(labelName);
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
