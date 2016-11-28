/*
#    This program is free software: you can redistribute it and/or modify
#    it under the terms of the GNU General Public License as published by
#    the Free Software Foundation, either version 3 of the License, or
#    (at your option) any later version.
#
#    This program is distributed in the hope that it will be useful,
#    but WITHOUT ANY WARRANTY; without even the implied warranty of
#    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#    GNU General Public License for more details.
#
#    You should have received a copy of the GNU General Public License
#    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package jchess.gui.secondary;

import org.jdesktop.application.*;
import org.jdesktop.application.Action;
import java.awt.*;
import javax.swing.*;

import jchess.*;
import jchess.util.TypedResourceBundle;

public class JChessAboutBox extends JDialog
{
	private static final long serialVersionUID = 8209938830184516667L;
	
	private static final TypedResourceBundle ABOUT_PROPERTIES = new TypedResourceBundle("jchess.resources.JChessAboutBox");
	private static final TypedResourceBundle APP_PROPERTIES = new TypedResourceBundle("jchess.resources.JChessApp");
	
	private JButton closeButton;
	
	private JLabel appTitleLabel;
	private JLabel versionTitelLabel;
	private JLabel versionLabel;
	private JLabel homepageTitelLabel;
	private JLabel homepageLabel;
	private JLabel appDescriptionLabel;
	private JLabel imageLabel;
	private JLabel activeDeveloperTitleLabel;
	private JLabel appVendorLabel1;
	private JLabel appHomepageLabel1;
	private JLabel activeDeveloperLabel;
	private JLabel inactiveDevelopersTitleLabelLabel;
	private JLabel inactiveDevelopersLabel;
	
	private ActionMap actionMap;
	
	public JChessAboutBox(Frame parent)
	{
		super(parent);
		initComponents();
		getRootPane().setDefaultButton(closeButton);
	}
	
	@Action
	public void closeAboutBox()
	{
		dispose();
	}
	
	private void initComponents()
	{
		initializeGuiElements();
		initializeWindow();
		initializeCloseButton();
		initializeLabels();
		initializeGroupLayout();
		
		pack();
	}

	private void initializeGuiElements()
	{
		closeButton = new JButton();
		appTitleLabel = new JLabel();
		versionTitelLabel = new JLabel();
		versionLabel = new JLabel();
		homepageTitelLabel = new JLabel();
		homepageLabel = new JLabel();
		appDescriptionLabel = new JLabel();
		imageLabel = new JLabel();
		activeDeveloperTitleLabel = new JLabel();
		appVendorLabel1 = new JLabel();
		appHomepageLabel1 = new JLabel();
		activeDeveloperLabel = new JLabel();
		inactiveDevelopersTitleLabelLabel = new JLabel();
		inactiveDevelopersLabel = new JLabel();
	}

	private void initializeWindow()
	{
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle(ABOUT_PROPERTIES.getString("title"));
		setModalityType(ModalityType.APPLICATION_MODAL);
		setName("aboutBox");
		setResizable(false);
	}

	private void initializeLabels()
	{
		setLabelContent(appTitleLabel, true, 4, "Application.title", "Application.title", APP_PROPERTIES);
		setLabelContent(appDescriptionLabel, false, 0, "appDescLabel.text", "appDescLabel", ABOUT_PROPERTIES);
		
		setLabelContent(versionTitelLabel, true, 0, "versionLabel.text", "versionLabel", ABOUT_PROPERTIES);
		setLabelContent(versionLabel, false, 0, "Application.version", "appVersionLabel", APP_PROPERTIES);
		
		setLabelContent(homepageTitelLabel, true, 0, "homepageLabel.text", "homepageLabel", ABOUT_PROPERTIES);
		setLabelContent(homepageLabel, false, 0, "Application.homepage", "appHomepageLabel", APP_PROPERTIES);
		
		imageLabel.setIcon(ABOUT_PROPERTIES.getIcon("imageLabel.icon")); // NOI18N
		imageLabel.setName("imageLabel"); // NOI18N
		
		setLabelContent(activeDeveloperTitleLabel, true, 0, "vendorLabel1.text", "vendorLabel1", ABOUT_PROPERTIES);
		setLabelContent(activeDeveloperLabel, false, 0, "appHomepageLabel2.text", "appHomepageLabel2", ABOUT_PROPERTIES);
		
		setLabelContent(inactiveDevelopersTitleLabelLabel, true, 0, "vendorLabel2.text", "vendorLabel2", ABOUT_PROPERTIES);
		setLabelContent(inactiveDevelopersLabel, false, 0, "appHomepageLabel3.text", "appHomepageLabel3", ABOUT_PROPERTIES);
	}
	
	private void setLabelContent(JLabel label, Boolean hasBoldFont, int fontEnlargement, String resourceName,
	        String labelName, TypedResourceBundle resourceBundle)
	{
		if(hasBoldFont)
		{
			label.setFont(label.getFont().deriveFont(label.getFont().getStyle() | Font.BOLD,
			        label.getFont().getSize() + fontEnlargement));
		}
		
		label.setText(resourceBundle.getString(resourceName)); // NOI18N
		label.setName(labelName); // NOI18N
	}
	
	private void initializeGroupLayout()
	{
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		setHorizontalLayoutGroup(layout);
		setVerticalLayoutGroup(layout);
	}
	
	private void initializeCloseButton()
	{
		actionMap = Application.getInstance(JChessApp.class).getContext().getActionMap(JChessAboutBox.class, this);
		closeButton.setAction(actionMap.get("closeAboutBox")); // NOI18N
		closeButton.setName("closeButton"); // NOI18N
	}
	
	private void setHorizontalLayoutGroup(GroupLayout layout)
	{
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addGroup(layout.createSequentialGroup().addComponent(imageLabel).addGap(18, 18, 18)
		                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
		                        .addComponent(appTitleLabel, GroupLayout.Alignment.LEADING).addComponent(appDescriptionLabel,
		                                GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 691, Short.MAX_VALUE)
		                        .addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addGroup(layout
		                                .createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(versionTitelLabel)
		                                .addComponent(
		                                        homepageTitelLabel)
		                                .addComponent(activeDeveloperTitleLabel).addComponent(inactiveDevelopersTitleLabelLabel))
		                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		                                        .addGroup(layout.createSequentialGroup().addGroup(layout
		                                                .createParallelGroup(GroupLayout.Alignment.LEADING)
		                                                .addComponent(appHomepageLabel1).addComponent(inactiveDevelopersLabel,
		                                                        GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE))
		                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		                                                .addComponent(closeButton))
		                                        .addComponent(versionLabel)
		                                        .addGroup(layout
		                                                .createParallelGroup(GroupLayout.Alignment.TRAILING, false)
		                                                .addComponent(appVendorLabel1, GroupLayout.Alignment.LEADING,
		                                                        GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
		                                                        Short.MAX_VALUE)
		                                                .addComponent(homepageLabel, GroupLayout.Alignment.LEADING,
		                                                        GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
		                                                        Short.MAX_VALUE)
		                                                .addComponent(activeDeveloperLabel, GroupLayout.DEFAULT_SIZE,
		                                                        GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
		                .addContainerGap()));
	}

	private void setVerticalLayoutGroup(GroupLayout layout)
	{
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		        .addComponent(imageLabel, GroupLayout.PREFERRED_SIZE, 194, Short.MAX_VALUE)
		        .addGroup(layout.createSequentialGroup().addContainerGap().addComponent(appTitleLabel)
		                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(appDescriptionLabel)
		                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(versionTitelLabel)
		                        .addComponent(versionLabel))
		                .addGap(27, 27, 27)
		                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(homepageTitelLabel)
		                        .addComponent(homepageLabel))
		                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		                        .addGroup(layout.createSequentialGroup()
		                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 41,
		                                        Short.MAX_VALUE)
		                                .addComponent(closeButton).addContainerGap())
		                        .addGroup(layout.createSequentialGroup()
		                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		                                        .addComponent(activeDeveloperTitleLabel).addComponent(appVendorLabel1)
		                                        .addComponent(activeDeveloperLabel))
		                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		                                        .addComponent(appHomepageLabel1)
		                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		                                                .addComponent(inactiveDevelopersTitleLabelLabel).addComponent(inactiveDevelopersLabel)))
		                                .addGap(36, 36, 36)))));
	}
}
