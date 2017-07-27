package net.pixael.jdt;

public class JDTString extends JDTBase {
	
	private static final long serialVersionUID = serialVersionUID();
	
	protected String value;
	
	public JDTString() {
		super(Type.STRING);
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getValue() {
		return this.value;
	}
	
	public String toString() {
		String value = this.value.replaceAll("\\\\", "\\\\\\\\");
		value = value.replaceAll("\"", "\\\\\"");
		value = value.replaceAll("\n", "\\n");
		value = value.replaceAll("\t", "\\t");
		return this.name + ":\"" + value + "\"";
	}
}
