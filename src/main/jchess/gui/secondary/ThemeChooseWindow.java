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

/*
 * Authors:
 * Mateusz Sławomir Lach ( matlak, msl )
 * Damian Marciniak
 */
package jchess.gui.secondary;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jchess.JChessApp;
import jchess.Localization;
import jchess.gui.ThemeConfigurator;
import jchess.gui.ThemeFileReader;
import jchess.gui.ThemeImageLoader;

public class ThemeChooseWindow extends JDialog implements ActionListener, ListSelectionListener
{
	private static final long serialVersionUID = -6866033656313872022L;
	private static Logger log = Logger.getLogger(ThemeChooseWindow.class.getName());
	
	private JList<String> themesList;
	private ImageIcon themePreviewImage;
	private JButton themePreviewButton;
	private JButton okButton;
	
	public ThemeChooseWindow(Frame parent) throws Exception
	{
		super(parent);
		
		ArrayList<String> themeNames = ThemeFileReader.getThemes();
		if(themeNames.size() > 0)
			createGUI(themeNames);
		else
			throw new Exception(Localization.getMessage("error_when_creating_theme_config_window"));
	}

	private void createGUI(ArrayList<String> themeNames)
	{
		// JList needs an array, so we convert it to one
		this.themesList = new JList<String>(themeNames.toArray(new String[themeNames.size()]));
		this.themePreviewButton = new JButton(this.themePreviewImage);
		this.okButton = new JButton("OK");
		
		setWindowProperties();
		setThemeListProperties();
		
		testGettingPreviewImage();
		
		setButtonProperties(themePreviewButton, 110, 10, 420, 120, false);
		setButtonProperties(okButton, 175, 140, 200, 50, true);
	}

	private void setWindowProperties()
	{
		this.setTitle(Localization.getMessage("choose_theme_window_title"));
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		Dimension winDim = new Dimension(550, 230);
		this.setMinimumSize(winDim);
		this.setMaximumSize(winDim);
		this.setSize(winDim);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	private void setThemeListProperties()
	{
		this.themesList.setLocation(new Point(10, 10));
		this.themesList.setSize(new Dimension(100, 120));
		this.add(this.themesList);
		this.themesList.setSelectionMode(0);
		this.themesList.addListSelectionListener(this);
	}

	private void testGettingPreviewImage()
	{
		try
		{
			this.themePreviewImage = new ImageIcon(ThemeImageLoader.loadThemeImage("Preview.png"));
		}
		catch(NullPointerException exception)
		{
			log.log(Level.SEVERE, "Cannot find preview image: ", exception);
			this.themePreviewImage = new ImageIcon(
			        JChessApp.class.getResource("/jchess/resources/theme/noPreview.png"));
		}
	}

	private void setButtonProperties(JButton button, int positionX, int positionY, int sizeX, int sizeY, Boolean isInteractable)
	{
		button.setLocation(new Point(positionX, positionY));
		button.setSize(new Dimension(sizeX, sizeY));
		this.add(button);
		
		if(isInteractable)
			button.addActionListener(this);
	}

	@Override
	public void valueChanged(ListSelectionEvent event)
	{
		String themeName = this.themesList.getModel().getElementAt(this.themesList.getSelectedIndex()).toString();
		this.themePreviewImage = new ImageIcon(ThemeImageLoader.loadThemeImage("Preview.png", themeName));
		this.themePreviewButton.setIcon(this.themePreviewImage);
	}

	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == this.okButton)
		{
			int selectedThemeIndex = this.themesList.getSelectedIndex();
			String themeName = this.themesList.getModel().getElementAt(selectedThemeIndex).toString();
			ThemeConfigurator.saveThemeConfiguration(themeName);
			
			JOptionPane.showMessageDialog(this, Localization.getMessage("changes_visible_after_restart"));
			this.setVisible(false);
		}
	}
}
