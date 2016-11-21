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
	private JLabel versionLabel;
	private JLabel appVersionLabel;
	private JLabel homepageLabel;
	private JLabel appHomepageLabel;
	private JLabel appDescriptionLabel;
	private JLabel imageLabel;
	private JLabel vendorLabel1;
	private JLabel appVendorLabel1;
	private JLabel appHomepageLabel1;
	private JLabel appHomepageLabel2;
	private JLabel vendorLabel2;
	private JLabel appHomepageLabel3;
	
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
		versionLabel = new JLabel();
		appVersionLabel = new JLabel();
		homepageLabel = new JLabel();
		appHomepageLabel = new JLabel();
		appDescriptionLabel = new JLabel();
		imageLabel = new JLabel();
		vendorLabel1 = new JLabel();
		appVendorLabel1 = new JLabel();
		appHomepageLabel1 = new JLabel();
		appHomepageLabel2 = new JLabel();
		vendorLabel2 = new JLabel();
		appHomepageLabel3 = new JLabel();
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
		setLabelContent(versionLabel, true, 0, "versionLabel.text", "versionLabel", ABOUT_PROPERTIES);
		setLabelContent(appVersionLabel, false, 0, "Application.version", "appVersionLabel", APP_PROPERTIES);
		setLabelContent(homepageLabel, true, 0, "homepageLabel.text", "homepageLabel", ABOUT_PROPERTIES);
		setLabelContent(appHomepageLabel, false, 0, "Application.homepage", "appHomepageLabel", APP_PROPERTIES);
		setLabelContent(appDescriptionLabel, false, 0, "appDescLabel.text", "appDescLabel", ABOUT_PROPERTIES);
		
		imageLabel.setIcon(ABOUT_PROPERTIES.getIcon("imageLabel.icon")); // NOI18N
		imageLabel.setName("imageLabel"); // NOI18N
		
		setLabelContent(vendorLabel1, true, 0, "vendorLabel1.text", "vendorLabel1", ABOUT_PROPERTIES);
		
		appVendorLabel1.setName("appVendorLabel1"); // NOI18N
		
		setLabelContent(appHomepageLabel1, false, 0, "appHomepageLabel1.text", "appHomepageLabel1", ABOUT_PROPERTIES);
		setLabelContent(appHomepageLabel2, false, 0, "appHomepageLabel2.text", "appHomepageLabel2", ABOUT_PROPERTIES);
		setLabelContent(vendorLabel2, true, 0, "vendorLabel2.text", "vendorLabel2", ABOUT_PROPERTIES);
		setLabelContent(appHomepageLabel3, false, 0, "appHomepageLabel3.text", "appHomepageLabel3", ABOUT_PROPERTIES);
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
		                                .createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(versionLabel)
		                                .addComponent(
		                                        homepageLabel)
		                                .addComponent(vendorLabel1).addComponent(vendorLabel2))
		                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		                                        .addGroup(layout.createSequentialGroup().addGroup(layout
		                                                .createParallelGroup(GroupLayout.Alignment.LEADING)
		                                                .addComponent(appHomepageLabel1).addComponent(appHomepageLabel3,
		                                                        GroupLayout.DEFAULT_SIZE, 466, Short.MAX_VALUE))
		                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		                                                .addComponent(closeButton))
		                                        .addComponent(appVersionLabel)
		                                        .addGroup(layout
		                                                .createParallelGroup(GroupLayout.Alignment.TRAILING, false)
		                                                .addComponent(appVendorLabel1, GroupLayout.Alignment.LEADING,
		                                                        GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
		                                                        Short.MAX_VALUE)
		                                                .addComponent(appHomepageLabel, GroupLayout.Alignment.LEADING,
		                                                        GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
		                                                        Short.MAX_VALUE)
		                                                .addComponent(appHomepageLabel2, GroupLayout.DEFAULT_SIZE,
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
		                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(versionLabel)
		                        .addComponent(appVersionLabel))
		                .addGap(27, 27, 27)
		                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(homepageLabel)
		                        .addComponent(appHomepageLabel))
		                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		                        .addGroup(layout.createSequentialGroup()
		                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 41,
		                                        Short.MAX_VALUE)
		                                .addComponent(closeButton).addContainerGap())
		                        .addGroup(layout.createSequentialGroup()
		                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		                                        .addComponent(vendorLabel1).addComponent(appVendorLabel1)
		                                        .addComponent(appHomepageLabel2))
		                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
		                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
		                                        .addComponent(appHomepageLabel1)
		                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
		                                                .addComponent(vendorLabel2).addComponent(appHomepageLabel3)))
		                                .addGap(36, 36, 36)))));
	}
}
