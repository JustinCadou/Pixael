package net.pixael;

import java.io.FileNotFoundException;

public class MissingResourcesException extends FileNotFoundException {
	
	private static final long serialVersionUID = 1L;
	
	public MissingResourcesException(String[] resources) {
		super(constructMessage(resources));
	}
	
	private static String constructMessage(String[] resources) {
		StringBuilder sb = new StringBuilder();
		sb.append("The following resource(s) are missing:\n");
		for (String resource : resources) {
			sb.append('\t').append(resource).append('\n');
		}
		return sb.toString();
	}
}
