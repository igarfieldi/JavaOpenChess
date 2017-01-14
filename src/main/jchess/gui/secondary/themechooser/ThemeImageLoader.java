package jchess.gui.secondary.themechooser;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import jchess.JChessApp;
import jchess.gamelogic.Player.Color;
import jchess.gamelogic.pieces.Piece;

public class ThemeImageLoader
{
	private static Logger log = Logger.getLogger(ThemeImageLoader.class.getName());
	
	private static ThemeImageLoader instance;
	
	private final Toolkit toolkit;
	private Map<String, Image> imageCache;
	private final Properties configFile;
	
	private ThemeImageLoader()
	{
		this.imageCache = new HashMap<String, Image>();
		this.configFile = ThemeConfigurator.getConfigFile();
		this.toolkit = Toolkit.getDefaultToolkit();
	}
	
	public static ThemeImageLoader getInstance()
	{
		if(instance == null)
			instance = new ThemeImageLoader();
		
		return instance;
	}
	
	/*
	 * Method load image by a given name with extension without specifiying the theme.
	 * 
	 * @name : string of image to load for ex. "chessboard.jpg"
	 * 
	 * @returns : image or null if cannot load
	 */
	public Image loadThemeImage(String imageName)
	{
		return loadThemeImage(imageName, this.configFile.getProperty("THEME", "default"));
	}
	
	/**
	 * Loads the image by a given name and theme.
	 * 
	 * @param imageName
	 * 				String of image to load for ex. "chessboard.jpg"
	 * @param theme
	 * 				String of the theme from which the image should be taken.
	 * @return
	 */
	public Image loadThemeImage(String imageName, String theme)
	{
		try
		{
			// Locate the image in the theme folder
			String imageLink = "/jchess/resources/theme/" + theme + "/images/" + imageName;
			return this.loadImageFromResources(imageLink);
		}
		catch(NullPointerException exc)
		{
			log.log(Level.SEVERE, "Failed to load image " + imageName + " from theme " + theme + ": ", exc);
			exc.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Loads the image of the specified piece.
	 * 
	 * @param piece
	 * 				The piece whose image should be loaded.
	 * @return
	 */
	public Image loadThemedPieceImage(Piece piece)
	{
		String imageName = piece.getName() + "-";
		imageName += getColorString(piece.getPlayer().getColor());
		imageName += ".png";
		
		return this.loadThemeImage(imageName);
	}
	
	/**
	 * Gets the string of the short version of a specified color.
	 * 
	 * @param color
	 * 				Color whose short version is needed.
	 * @return string of short version of the color.
	 */
	public String getColorString(Color color)
	{
		String colorString = "";
		switch(color)
		{
			case WHITE:
				colorString += "W";
				break;
			case RED:
				colorString += "BR";
				break;
			case BLACK:
				colorString += "B";
				break;
			case GOLDEN:
				colorString += "G";
				break;
			default:
				log.log(Level.SEVERE, "Piece of player with unknown color exists!");
		}
		return colorString;
	}
	
	/**
	 * Loads an image inside the specified resource path.
	 * 
	 * @param path
	 * 				The file path of the resource.
	 * @return image inside the resource path.
	 */
	private Image loadImageFromResources(String path)
	{
		Image img = imageCache.get(path);
		if(img == null)
		{
			// If the image is not yet in the cache, load it
			try
			{
				img = toolkit.getImage(JChessApp.class.getResource(path));
				imageCache.put(path, img);
			}
			catch(NullPointerException exc)
			{
				log.log(Level.SEVERE, "Failed to load image: " + path + "!", exc);
				return null;
			}
		}
		return img;
	}
}
