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

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.event.ListSelectionListener;

import jchess.JChessApp;
import jchess.Localization;
import jchess.gamelogic.Settings;
import jchess.gui.GUI;

import javax.swing.event.ListSelectionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThemeChooseWindow extends JDialog implements ActionListener, ListSelectionListener
{

    JList<String> themesList;
    ImageIcon themePreview;
    GridBagLayout gbl;
    GridBagConstraints gbc;
    JButton themePreviewButton;
    JButton okButton;

    public ThemeChooseWindow(Frame parent) throws Exception
    {
        super(parent);
        
        ArrayList<String> themeNames = new ArrayList<String>();
        
        java.security.CodeSource src = JChessApp.class.getProtectionDomain().getCodeSource();
    	if(src != null)
    	{
    		File jarPath = new File(src.getLocation().getPath());
    		if(jarPath.isDirectory())
    		{
    			// We're not in a JAR, but instead in some directory
    			// => Scan through the directories to find the themes
    			File themePath = new File(jarPath.getAbsolutePath() + File.separator + "jchess" +
    									File.separator + "resources" + File.separator + "theme");
    			File[] themeFiles = themePath.listFiles();
    			for(int i = 0; i < themeFiles.length; i++)
    			{
    				if(themeFiles[i].isDirectory())
    				{
    					themeNames.add(themeFiles[i].getName());
    				}
    			}
    		}
    		else
    		{
    			// We're in a JAR
    			// General pattern to extract a theme's name
        		Pattern themePattern = Pattern.compile("/[^/]+/$");
        		
    			java.net.URL jar = src.getLocation();
    			java.util.zip.ZipInputStream zip = new java.util.zip.ZipInputStream(jar.openStream());
    			java.util.zip.ZipEntry entry;
    			
    			while((entry = zip.getNextEntry()) != null)
    			{
    				// Find the resources we're looking for (the themes)
    				// TODO: If the beginning of the path is not specified we get lots of double matches...
    				if(entry.getName().matches("jchess/resources/theme/[^/]+/$"))
    				{
    					Matcher themeMatcher = themePattern.matcher(entry.getName());
    					if(themeMatcher.find())
    					{
    						// Remove the '/' from the path
    						themeNames.add(themeMatcher.group(0).replace("/", ""));
    					}
    				}
    			}
    		}
    	}
    	else
    	{
            throw new Exception(Localization.getMessage("error_when_creating_theme_config_window"));
    	}
    	
        if(themeNames.size() > 0)
        {
            this.setTitle(Localization.getMessage("choose_theme_window_title"));
            Dimension winDim = new Dimension(550, 230);
            this.setMinimumSize(winDim);
            this.setMaximumSize(winDim);
            this.setSize(winDim);
            this.setResizable(false);
            this.setLayout(null);
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            
            // JList needs an array, so we convert it to one
            this.themesList = new JList<String>(themeNames.toArray(new String[themeNames.size()]));
            this.themesList.setLocation(new Point(10, 10));
            this.themesList.setSize(new Dimension(100, 120));
            this.add(this.themesList);
            this.themesList.setSelectionMode(0);
            this.themesList.addListSelectionListener(this);
            
            this.gbl = new GridBagLayout();
            this.gbc = new GridBagConstraints();
            try
            {
                this.themePreview = new ImageIcon(GUI.loadThemeImage("Preview.png"));
            }
            catch (java.lang.NullPointerException exc)
            {
                System.err.println("Cannot find preview image: " + exc);
                this.themePreview = new ImageIcon(JChessApp.class.getResource("/jchess/resources/theme/noPreview.png"));
                return ;
            }
            this.themePreviewButton = new JButton(this.themePreview);
            this.themePreviewButton.setLocation(new Point(110, 10));
            this.themePreviewButton.setSize(new Dimension(420, 120));
            this.add(this.themePreviewButton);
            this.okButton = new JButton("OK");
            this.okButton.setLocation(new Point(175, 140));
            this.okButton.setSize(new Dimension(200, 50));
            this.add(this.okButton);
            this.okButton.addActionListener(this);
            this.setModal(true);
        }
        else
        {
            throw new Exception(Localization.getMessage("error_when_creating_theme_config_window"));
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent event)
    {
        String themeName = this.themesList.getModel().getElementAt(this.themesList.getSelectedIndex()).toString();
        this.themePreview = new ImageIcon(GUI.loadThemeImage("Preview.png", themeName));
        this.themePreviewButton.setIcon(this.themePreview);
    }

    /** Method which is changing a pawn into queen, rook, bishop or knight
     * @param arg0 Capt information about performed action
     */
    public void actionPerformed(ActionEvent evt)
    {
        if (evt.getSource() == this.okButton)
        {
            Properties prp = GUI.getConfigFile();
            int element = this.themesList.getSelectedIndex();
            String name = this.themesList.getModel().getElementAt(element).toString();
            if (GUI.themeIsValid(name))
            {
                prp.setProperty("THEME", name);
                if(!GUI.storeConfigFile(prp))
                {
                	System.err.println("Failed to save config with new theme!");
                }
                JOptionPane.showMessageDialog(this, Localization.getMessage("changes_visible_after_restart"));
                this.setVisible(false);

            }
            System.out.print(prp.getProperty("THEME"));
        }
    }
}
