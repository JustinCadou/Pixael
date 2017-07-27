package net.pixael.jdt;

public class JSONSyntaxException extends RuntimeException {
	
	private static final long serialVersionUID = 521625649193547L;
	
	public JSONSyntaxException(String message, String src, int errIndex) {
		super(constructMessage(message, src, errIndex));
	}
	
	private static String constructMessage(String msg, String src, int err) {
		StringBuilder sb = new StringBuilder();
		sb.append(msg);
		sb.append("\n\t\t");
		sb.append(src);
		sb.append("\n\t\t");
		for (int i = 0; i < err; i++) {
			sb.append(" ");
		}
		sb.append("^");
		return sb.toString();
	}
}
