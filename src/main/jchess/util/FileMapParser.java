package jchess.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileMapParser
{
	private Map<String, String> properties;
	
	public FileMapParser() {
		this.properties = new HashMap<String, String>();
	}
	
	public void save(File file) throws IOException {
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
			for(Map.Entry<String, String> entry : this.properties.entrySet()) {
				System.out.println(entry.getKey());
				writer.write('[' + entry.getKey() + ' ' + entry.getValue() + ']');
				writer.newLine();
			}
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
					// Property line found
					String[] keyValuePair = line.split(" ", 2);
					String key = keyValuePair[0].substring(1);
					String value = keyValuePair[1];
					if(value.charAt(value.length() - 1) == ']') {
						// Remove trailing property indicator
						value = value.substring(0, value.length() - 1);
					}
					properties.put(key, value);
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
}
