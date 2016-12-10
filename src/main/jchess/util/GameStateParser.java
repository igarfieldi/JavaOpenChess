package jchess.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameStateParser
{
	private Map<String, String> properties;
	private String moves;
	
	public GameStateParser() {
		this.properties = new HashMap<String, String>();
		this.moves = new String("");
	}
	
	public void save(File file) throws IOException {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			for(Map.Entry<String, String> entry : this.properties.entrySet()) {
				writer.write('[' + entry.getKey() + ' ' + entry.getValue() + ']');
				writer.newLine();
			}
			writer.newLine();
			writer.write(moves);
		} catch(IOException exc) {
			// Rethrow the exception; we only catch it to close the writer anyway
			throw exc;
		}
	}
	
	public void load(File file) throws IOException {
		try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			
			while((line = reader.readLine()) != null) {
				if(!line.isEmpty() && line.charAt(0) == '[') {
					String[] keyValuePair = line.split(" ");
					properties.put(keyValuePair[0].substring(1),
							keyValuePair[1].substring(0, keyValuePair[1].length() - 1));
				} else if(!line.isEmpty()) {
					moves += line + '\n';
				}
			}
		} catch(IOException exc) {
			// Rethrow the exception; we only catch it to close the reader anyway
			throw exc;
		}
	}
	
	public String getProperty(String key) {
		return properties.get(key);
	}
	
	public String setProperty(String key, String value) {
		return this.properties.put(key, value);
	}
	
	public String getProperties() {
		String allProperties = "";
		for(String val : this.properties.values()) {
			allProperties += val;
		}
		return allProperties;
	}
	
	public String getMoves() {
		return moves;
	}
	
	public void setNoves(String moves) {
		this.moves = moves;
	}
}
