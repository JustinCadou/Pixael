package net.pixael.jdt;

public class JDTShort extends JDTBase {
	
	private static final long serialVersionUID = serialVersionUID();
	
	protected Short value;
	
	public JDTShort() {
		super(Type.SHORT);
	}
	
	public void setValue(Short value) {
		this.value = value;
	}
	
	public Short getValue() {
		return this.value;
	}
	
	public String toString() {
		return super.toString() + "s";
	}
}
