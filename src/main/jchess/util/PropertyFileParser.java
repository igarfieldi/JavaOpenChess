package jchess.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class PropertyFileParser
{
	private BufferedReader reader;
	private Map<String, String> properties;
	private String body;
	
	public PropertyFileParser(File file) throws IOException {
		this.reader = new BufferedReader(new FileReader(file));
		this.properties = new HashMap<String, String>();
		this.body = new String("");
		
		this.parseFile();
	}
	
	private void parseFile() throws IOException {
		String line;
		
		while((line = reader.readLine()) != null) {
			if(!line.isEmpty() && line.charAt(0) == '[') {
				String[] keyValuePair = line.split(" ");
				properties.put(keyValuePair[0].substring(1),
						keyValuePair[1].substring(0, keyValuePair[1].length() - 1));
			} else if(!line.isEmpty()) {
				body += line + '\n';
			}
		}
	}
	
	public String getProperty(String key) {
		return properties.get(key);
	}
	
	public String getProperties() {
		String allProperties = "";
		for(String val : this.properties.values()) {
			allProperties += val;
		}
		return allProperties;
	}
	
	public String getBody() {
		return body;
	}
}
