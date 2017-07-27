package net.pixael.client;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import org.lwjgl.Version;
import net.fantasticfantasy.mainkit.AbstractUtil;
import net.pixael.Pixael;
import net.pixael.util.ResourcesUtil;

public class Crash implements Serializable {
	
	private static final long serialVersionUID = 1001;
	
	private String title, details;
	
	public Crash(String title, String details) {
		this.title = title;
		this.details = details;
	}
	
	public void send() {
		System.out.println("\n --------------------------------------------------\n");
		String header = "\t>>> " + AbstractUtil.getTime(":") + " - " + this.title + " <<<";
		StringBuilder footer = new StringBuilder();
		footer.append("GAME_VERSION: ");
		footer.append(Pixael.getVersion());
		footer.append("\nLWJGL_VERSION: ");
		footer.append(Version.getVersion());
		footer.append("\n\n\nPlease report this error at:");
		footer.append("\nwww.fantasticfantasy.net/pixael/bugreports/");
		footer.append("\n\nSorry for the inconvenience,");
		footer.append("\nPixael");
		System.out.println(header);
		System.out.println();
		System.out.println(this.details);
		System.out.println();
		System.out.println(footer);
		String filename = this.title.replaceAll(" ", "_") + "-" + AbstractUtil.getTime("_") + ".crashlog";
		String path = ResourcesUtil.getUserResourcesDirectory() + "crashreports\\";
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(path + filename)))) {
			writer.write(header);
			writer.newLine();
			writer.newLine();
			String[] lines = this.details.split("\n");
			for (String line : lines) {
				writer.write(line);
				writer.newLine();
			}
			writer.newLine();
			String[] flines = footer.toString().split("\n");
			for (String fline : flines) {
				writer.write(fline);
				writer.newLine();
			}
			writer.flush();
		} catch (IOException e) {
			System.out.println("Unable to send crash " + this.title + ", closing Pixael anyway!");
		} finally {
			System.out.println("\n --------------------------------------------------\n");
			System.exit(-1);
		}
	}
	
	public static String throwableToString(Throwable t) {
		String start = "Exception in thread '" + Thread.currentThread().getName()
				+ "': " + t.getClass().getName() + (t.getMessage() == null ? "" : ": " + t.getMessage());
		StringBuilder sb = new StringBuilder();
		StackTraceElement[] stackTrace = t.getStackTrace();
		for (StackTraceElement element : stackTrace) {
			sb.append("\n\tat " + element.toString());
		}
		return start + sb.toString();
	}
}
