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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import jchess.gui.setup.LocalSettingsGUI;

/**
 * Class representing the game interface which is seen by a player and where are
 * lockated available for player opptions, current games and where can he start
 * a new game (load it or save it)
 */
public class ThemeConfigurator
{
	private static Logger log = Logger.getLogger(LocalSettingsGUI.class.getName());
	
	public static Properties getConfigFile()
	{
		Properties configuration = new Properties();
		// Configuration files cannot be part of the JAR, so it's just a file in
		// the same directory
		File configFile = new File(ThemeConfigurator.getJarPath() + File.separator + "config.txt");
		log.info("Configuration file: " + configFile);
		
		if(!configFile.exists())
			ThemeConfigurator.storeConfigFile(configuration);
		
		loadThemeConfiguration(configuration, configFile);
		
		return configuration;
	}

	private static String getJarPath()
	{
		try
		{
			File jar = new File(ThemeConfigurator.class.getProtectionDomain().getCodeSource().getLocation().toURI());
			if(jar.isDirectory())
				return jar.getAbsolutePath();
			else
				return jar.getParent();
		}
		catch(SecurityException | NullPointerException | URISyntaxException exception)
		{
			// Possible issues:
			// 1. No permission to get protection domain
			// 2. Code source might be null
			// 3. JAR URI cannot be converted (pathological case)
			log.log(Level.SEVERE, "Failed to locate JAR execution path; Using current working directory instead!", exception);
			return ".";
		}
	}

	private static void loadThemeConfiguration(Properties configuration, File configFile)
	{
		try(InputStream inputStream = new FileInputStream(configFile))
		{
			configuration.load(inputStream);
			log.info("Active theme: " + configuration.getProperty("THEME", "default"));
		}
		catch(IOException exception)
		{
			// This shouldn't happen - if the file didn't exist before we
			// created it above
			log.log(Level.SEVERE, "Could not load configuration file: ", exception);
			exception.printStackTrace();
		}
	}
	
	private static boolean storeConfigFile(Properties configuration)
	{
		File configFile = new File(ThemeConfigurator.getJarPath() + File.separator + "config.txt");
		try(OutputStream configFileStream = new FileOutputStream(configFile))
		{
			configuration.store(configFileStream, null);
			return true;
		}
		catch(IOException exception)
		{
			log.log(Level.SEVERE, "Failed to create config file!", exception);
			return false;
		}
	}
	
	public static void saveThemeConfiguration(String themeName)
	{
		Properties properties = getConfigFile();
		
		properties.setProperty("THEME", themeName);
		if(!storeConfigFile(properties))
			log.log(Level.SEVERE, "Failed to save config with new theme!");
			
		log.info(properties.getProperty("THEME"));
	}
}
