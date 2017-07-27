package net.pixael.jdt;

import java.io.IOException;
import java.io.Writer;

public class JSONIOTools {
	
	public static void write(JDTObject jdt, Writer out) throws IOException {
		out.write(jdt.toString());
		out.flush();
	}
}
