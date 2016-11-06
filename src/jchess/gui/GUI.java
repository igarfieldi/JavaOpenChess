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
package jchess.gui;

import java.awt.*;
import java.net.*;
import java.io.*;

import java.util.Properties;

import jchess.JChessApp;
import jchess.gamelogic.Game;

/** Class representing the game interface which is seen by a player and
 * where are lockated available for player opptions, current games and where
 * can he start a new game (load it or save it)
 */
public class GUI
{

    public Game game;
    static final public Properties configFile = GUI.getConfigFile();

    public GUI()
    {
        this.game = new Game();

        //this.drawGUI();
    }/*--endOf-GUI--*/

    /*Method load image by a given name with extension
     * @name     : string of image to load for ex. "chessboard.jpg"
     * @returns  : image or null if cannot load
     * */

    public static Image loadThemeImage(String imageName)
    {
        return GUI.loadThemeImage(imageName, configFile.getProperty("THEME", "default"));
    }/*--endOf-loadImage--*/

    public static Image loadThemeImage(String imageName, String theme)
    {
    	// TODO: is this necessary? Shouldn't we create a config file if none exists or work with default values?
        if (configFile == null) {
            return null;
        }
        
        Toolkit tk = Toolkit.getDefaultToolkit();
        try
        {
        	// Locate the image in the theme folder
            String imageLink = "/jchess/resources/theme/" + theme + "/images/" + imageName;
            return tk.getImage(JChessApp.class.getResource(imageLink));
        }
        catch (Exception exc)
        {
            System.err.println("Failed to load image " + imageName + " from theme " + theme + ": " + exc);
            exc.printStackTrace();
        }
        return null;
    }

    public static boolean themeIsValid(String name)
    {
    	// LUL
        return true;
    }

    static String getJarPath()
    {
    	try
    	{
	    	File jar = new File(GUI.class.getProtectionDomain().getCodeSource().getLocation().toURI());
	    	if(jar.isDirectory()) {
	    		return jar.getAbsolutePath();
	    	} else {
	    		return jar.getParent();
	    	}
    	}
    	catch(SecurityException|NullPointerException|URISyntaxException exc) {
    		// Possible issues:
    		// 1. No permission to get protection domain
    		// 2. Code source might be null
    		// 3. JAR URI cannot be converted (pathological case)
    		System.out.println("Failed to locate JAR execution path; Using current working directory instead!");
    		return ".";
    	}
    }

    public static Properties getConfigFile()
    {
        Properties configuration = new Properties();
        // Configuration files cannot be part of the JAR, so it's just a file in the same directory
        File configFile = new File(GUI.getJarPath() + File.separator + "config.txt");
        System.out.println("Configuration file: " + configFile);
        
        if (!configFile.exists()) {
            GUI.storeConfigFile(configuration);
        }
        try (InputStream is = new FileInputStream(configFile))
        {
        	configuration.load(is);
        	System.out.println("Active theme: " + configuration.getProperty("THEME", "default"));
        }
        catch (IOException exc)
        {
        	// This shouldn't happen - if the file didn't exist before we created it above
            System.err.println("Could not load configuration file: " + exc);
            exc.printStackTrace();
        }
        return configuration;
    }
    
    public static boolean storeConfigFile(Properties configuration)
    {
        File configFile = new File(GUI.getJarPath() + File.separator + "config.txt");
    	try (OutputStream configFileStream = new FileOutputStream(configFile))
        {
        	configuration.store(configFileStream, null);
        	return true;
        }
        catch (java.io.IOException exc)
        {
        	System.err.println("Failed to create config file!");
        	return false;
        }
    }
}
