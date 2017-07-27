package net.pixael.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class ResourcesUtil {
	
	public static final int TYPE_ANY = 0,
							TYPE_DIRECTORY = 1,
							TYPE_FILE = 2;
	
	private static String userDirectory;
	
	public static boolean makeUnexistingFilesAndDirectories() {
		boolean zero = make(userDirectory + "plugins\\");
		boolean one = make(userDirectory + "worlds\\");
		boolean two = make(userDirectory + "crashreports\\");
		boolean three = make(userDirectory + "versions\\");
		return zero | one | two | three;
	}
	
	public static boolean make(String pathname) {
		File file = new File(pathname);
		if (file.exists()) {
			return false;
		}
		boolean dir = file.mkdirs();
		if (file.isDirectory()) {
			return dir;
		} else {
			try {
				return file.createNewFile();
			} catch (IOException ioe) {
				return false;
			}
		}
	}
	
	public static void setUserDirectory(String path) {
		userDirectory = path;
	}
	
	public static String getUserResourcesDirectory() {
		return userDirectory;
	}
	
	public static String getUserHome() {
		return System.getProperty("user.home");
	}
	
	public static boolean validate(String pathname, int type) {
		File file = new File(pathname);
		if (type == TYPE_ANY) {
			return file.exists();
		} else if (type == TYPE_DIRECTORY) {
			return file.isDirectory();
		} else if (type == TYPE_FILE) {
			return file.isFile();
		} else {
			throw new IllegalArgumentException("Invalid type '" + type + "'!");
		}
	}
	
	public static InputStream createJarResourceInputStream(String path) {
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
	}
	
	public static InputStream createAssetInputStream(String path) {
		return createJarResourceInputStream("\\asset\\pixael\\" + path);
	}
}
