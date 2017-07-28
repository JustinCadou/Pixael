package net.pixael.render;

public class GLSLSyntaxException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public GLSLSyntaxException(String shaderName, String details) {
		super("Error in shader '" + shaderName + "'!\n" + details);
	}
}
