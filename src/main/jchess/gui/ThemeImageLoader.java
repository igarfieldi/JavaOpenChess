package jchess.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import jchess.JChessApp;
import jchess.gamelogic.Player;
import jchess.gamelogic.pieces.Piece;

public class ThemeImageLoader
{
	private static Logger log = Logger.getLogger(ThemeImageLoader.class.getName());
	static final private Properties configFile = ThemeConfigurator.getConfigFile();
	
	/*
	 * Method load image by a given name with extension
	 * 
	 * @name : string of image to load for ex. "chessboard.jpg"
	 * 
	 * @returns : image or null if cannot load
	 */
	public static Image loadThemeImage(String imageName)
	{
		return loadThemeImage(imageName, configFile.getProperty("THEME", "default"));
	}
	
	public static Image loadThemeImage(String imageName, String theme)
	{
		// TODO: is this necessary? Shouldn't we create a config file if none
		// exists or work with default values?
		if(configFile == null)
			return null;
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		try
		{
			// Locate the image in the theme folder
			String imageLink = "/jchess/resources/theme/" + theme + "/images/" + imageName;
			return toolkit.getImage(JChessApp.class.getResource(imageLink));
		}
		catch(Exception exception)
		{
			log.log(Level.SEVERE, "Failed to load image " + imageName + " from theme " + theme + ": ", exception);
			exception.printStackTrace();
		}
		return null;
	}
	
	public static Image loadThemedPieceImage(Piece piece) {
		// TODO: cache images!
		String imageName = piece.getName() + "-";
		if(piece.getPlayer().getColor() == Player.Color.WHITE) {
			imageName += "W";
		} else {
			imageName += "B";
		}
		imageName += ".png";
		
		return ThemeImageLoader.loadThemeImage(imageName);
	}
}
