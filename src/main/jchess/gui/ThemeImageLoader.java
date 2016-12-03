package jchess.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import jchess.JChessApp;
import jchess.gamelogic.Player;
import jchess.gamelogic.pieces.Piece;

public class ThemeImageLoader
{
	private static Logger log = Logger.getLogger(ThemeImageLoader.class.getName());
	
	private static ThemeImageLoader instance;

	private final Toolkit toolkit;
	private Map<String, Image> imageCache;
	private final Properties configFile;
	
	private ThemeImageLoader() {
		this.imageCache = new HashMap<String, Image>();
		this.configFile = ThemeConfigurator.getConfigFile();
		this.toolkit = Toolkit.getDefaultToolkit();
	}
	
	public static ThemeImageLoader getInstance() {
		if(instance == null) {
			instance = new ThemeImageLoader();
		}
		return instance;
	}
	
	/*
	 * Method load image by a given name with extension
	 * 
	 * @name : string of image to load for ex. "chessboard.jpg"
	 * 
	 * @returns : image or null if cannot load
	 */
	public Image loadThemeImage(String imageName)
	{
		return loadThemeImage(imageName, this.configFile.getProperty("THEME", "default"));
	}
	
	public Image loadThemeImage(String imageName, String theme)
	{
		// TODO: is this necessary? Shouldn't we create a config file if none
		// exists or work with default values?
		if(this.configFile == null)
			return null;
		
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
	
	public Image loadThemedPieceImage(Piece piece) {
		String imageName = piece.getName() + "-";
		if(piece.getPlayer().getColor() == Player.Color.WHITE) {
			imageName += "W";
		} else {
			imageName += "B";
		}
		imageName += ".png";
		
		return this.loadThemeImage(imageName);
	}
	
	private Image loadImageFromResources(String path) {
		Image img = imageCache.get(path);
		if(img == null) {
			// If the image is not yet in the cache, load it
			img = toolkit.getImage(JChessApp.class.getResource(path));
			imageCache.put(path, img);
		}
		return img;
	}
}
