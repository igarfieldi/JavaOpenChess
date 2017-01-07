package jchess.gui.secondary.themechooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jchess.JChessApp;
import jchess.Localization;

public class ThemeFileReader
{
	private static ArrayList<String> themes = new ArrayList<String>();
	
	public static ArrayList<String> getThemes() throws IOException, Exception
	{
		CodeSource codeSource = JChessApp.class.getProtectionDomain().getCodeSource();
		if(codeSource != null)
		{
			File jarPath = new File(codeSource.getLocation().toURI().getPath());
			if(jarPath.isDirectory())
				themes = getThemesFromDirectory(jarPath);
			else
				themes = getThemesFromJAR(codeSource);
			
			return themes;
		}
		else
			throw new Exception(Localization.getMessage("error_when_creating_theme_config_window"));
	}

	private static ArrayList<String> getThemesFromDirectory(File jarPath)
	{
		ArrayList<String> themeList = new ArrayList<String>();
		File themePath = new File(jarPath.getAbsolutePath() + File.separator + "jchess" + File.separator
		        + "resources" + File.separator + "theme");
		File[] themeFiles = themePath.listFiles();
		for(int i = 0; i < themeFiles.length; i++)
		{
			if(themeFiles[i].isDirectory()) {
				themeList.add(themeFiles[i].getName());
			}
		}
		
		return themeList;
	}

	private static ArrayList<String> getThemesFromJAR(CodeSource codeSource) throws IOException
	{
		ArrayList<String> themeList = new ArrayList<String>();
		Pattern themePattern = Pattern.compile("/[^/]+/$");
		
		URL jar = codeSource.getLocation();
		ZipInputStream zip = new ZipInputStream(jar.openStream());
		ZipEntry entry;
		
		while((entry = zip.getNextEntry()) != null)
		{
			// Find the resources we're looking for (the themes)
			if(entry.getName().matches("jchess/resources/theme/[^/]+/$"))
			{
				Matcher themeMatcher = themePattern.matcher(entry.getName());
				if(themeMatcher.find())
					themeList.add(themeMatcher.group(0).replace("/", ""));
			}
		}
		
		return themeList;
	}
}
