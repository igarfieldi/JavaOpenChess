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
package jchess.gui.secondary.themechooser;

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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jchess.JChessApp;
import jchess.Localization;

/**
 * Class with a dialog containing UI elements to change the game's theme.
 */
public class ThemeChooseWindow extends JDialog implements ActionListener, ListSelectionListener
{
	private static final long serialVersionUID = -6866033656313872022L;
	private static Logger log = Logger.getLogger(ThemeChooseWindow.class.getName());
	
	private JList<String> themesList;
	private ImageIcon themePreviewImage;
	private JButton themePreviewButton;
	private JButton okButton;
	
	private static final int EMPTY = 0;
	
	private static final Point THEME_PREVIEW_BUTTON_LOCATION = new Point(110, 10);
	private static final Dimension THEME_PREVIEW_BUTTON_DIMENSION = new Dimension(420, 120);
	
	private static final Point OK_BUTTON_LOCATION = new Point(175, 140);
	private static final Dimension OK_BUTTON_DIMENSION = new Dimension(200, 50);
	
	public ThemeChooseWindow(Frame parent) throws Exception
	{
		super(parent);
		
		ArrayList<String> themeNames = ThemeFileReader.getThemes();
		if(themeNames.size() > EMPTY)
			createGUI(themeNames);
		else
			throw new Exception(Localization.getMessage("error_when_creating_theme_config_window"));
	}

	/**
	 * Creates instances of all UI elements and sets their properties.
	 * 
	 * @param themeNames
	 * 				List of all themes.
	 */
	private void createGUI(ArrayList<String> themeNames)
	{
		// JList needs an array, so we convert it to one
		this.themesList = new JList<String>(themeNames.toArray(new String[themeNames.size()]));
		this.themePreviewButton = new JButton(this.themePreviewImage);
		this.okButton = new JButton("OK");
		
		setWindowProperties();
		
		setThemeListProperties();
		setThemePreviewImage();
		
		setButtonProperties(themePreviewButton, THEME_PREVIEW_BUTTON_LOCATION, THEME_PREVIEW_BUTTON_DIMENSION, false);
		setButtonProperties(okButton, OK_BUTTON_LOCATION, OK_BUTTON_DIMENSION, true);
	}

	/**
	 * Sets the properties of the dialog.
	 */
	private void setWindowProperties()
	{
		final Dimension SIZE = new Dimension(550, 230);
		
		this.setTitle(Localization.getMessage("choose_theme_window_title"));
		this.setModalityType(ModalityType.APPLICATION_MODAL);
		this.setMinimumSize(SIZE);
		this.setMaximumSize(SIZE);
		this.setSize(SIZE);
		this.setResizable(false);
		this.setLayout(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	/**
	 * Sets the properties of the list of themes.
	 */
	private void setThemeListProperties()
	{
		this.themesList.setLocation(new Point(10, 10));
		this.themesList.setSize(new Dimension(100, 120));
		this.add(this.themesList);
		this.themesList.setSelectionMode(0);
		this.themesList.addListSelectionListener(this);
	}

	/**
	 * Creates instance of the preview image UI element and gives it the actual image.
	 */
	private void setThemePreviewImage()
	{
		try
		{
			this.themePreviewImage = new ImageIcon(ThemeImageLoader.getInstance().loadThemeImage("Preview.png"));
		}
		catch(NullPointerException exception)
		{
			log.log(Level.SEVERE, "Cannot find preview image: ", exception);
			this.themePreviewImage = new ImageIcon(
			        JChessApp.class.getResource("/jchess/resources/theme/noPreview.png"));
		}
	}

	/**
	 * Sets the properties of a specified button
	 * 
	 * @param button
	 * 				The button whose properties should be set.
	 * @param location
	 * 				The position of the button.
	 * @param size
	 * 				The size of the button
	 * @param isInteractable
	 * 				Boolean to determine whether the button is active or not.
	 */
	private void setButtonProperties(JButton button, Point location, Dimension size, Boolean isInteractable)
	{
		button.setLocation(location);
		button.setSize(size);
		this.add(button);
		
		if(isInteractable)
			button.addActionListener(this);
	}

	@Override
	public void valueChanged(ListSelectionEvent event)
	{
		String themeName = this.themesList.getModel().getElementAt(this.themesList.getSelectedIndex()).toString();
		this.themePreviewImage = new ImageIcon(ThemeImageLoader.getInstance().loadThemeImage("Preview.png", themeName));
		this.themePreviewButton.setIcon(this.themePreviewImage);
	}

	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == this.okButton)
		{
			int selectedThemeIndex = this.themesList.getSelectedIndex();
			String themeName = this.themesList.getModel().getElementAt(selectedThemeIndex).toString();
			ThemeConfigurator.saveThemeConfiguration(themeName);
			
			this.setVisible(false);
			this.getParent().repaint();
		}
	}
}
