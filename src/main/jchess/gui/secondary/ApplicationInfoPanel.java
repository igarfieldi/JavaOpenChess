package jchess.gui.secondary;

import java.awt.Font;
import java.awt.GridBagConstraints;

import javax.swing.JLabel;

import jchess.gui.setup.GridBagPanel;
import jchess.util.TypedResourceBundle;

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
		setLabelContent(appTitleLabel, true, 4, "Application.title", "Application.title", APP_PROPERTIES);
		setLabelContent(appDescriptionLabel, false, 0, "appDescLabel.text", "appDescLabel", ABOUT_PROPERTIES);
		
		setLabelContent(versionTitelLabel, true, 0, "versionLabel.text", "versionLabel", ABOUT_PROPERTIES);
		setLabelContent(versionLabel, false, 0, "Application.version", "appVersionLabel", APP_PROPERTIES);
		
		setLabelContent(homepageTitelLabel, true, 0, "homepageLabel.text", "homepageLabel", ABOUT_PROPERTIES);
		setLabelContent(homepageLabel, false, 0, "Application.homepage", "appHomepageLabel", APP_PROPERTIES);
		
		setLabelContent(activeDeveloperTitleLabel, true, 0, "vendorLabel1.text", "vendorLabel1", ABOUT_PROPERTIES);
		setLabelContent(activeDeveloperLabel, false, 0, "appHomepageLabel2.text", "appHomepageLabel2",
		        ABOUT_PROPERTIES);
		
		setLabelContent(inactiveDevelopersTitleLabelLabel, true, 0, "vendorLabel2.text", "vendorLabel2",
		        ABOUT_PROPERTIES);
		setLabelContent(inactiveDevelopersLabel, false, 0, "appHomepageLabel3.text", "appHomepageLabel3",
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
		
		setGridBagConstraints(appTitleLabel, 0, 0);
		setGridBagConstraints(appDescriptionLabel, 0, 1);
		
		setGridBagConstraints(versionTitelLabel, 0, 2);
		setGridBagConstraints(versionLabel, 1, 2);
		
		setGridBagConstraints(homepageTitelLabel, 0, 3);
		setGridBagConstraints(homepageLabel, 1, 3);
		
		setGridBagConstraints(activeDeveloperTitleLabel, 0, 4);
		setGridBagConstraints(activeDeveloperLabel, 1, 4);
		
		setGridBagConstraints(inactiveDevelopersTitleLabelLabel, 0, 5);
		setGridBagConstraints(inactiveDevelopersLabel, 1, 5);
	}
}
