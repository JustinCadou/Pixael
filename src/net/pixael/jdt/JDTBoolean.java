package net.pixael.jdt;

public class JDTBoolean extends JDTBase {
	
	private static final long serialVersionUID = serialVersionUID();
	
	protected Boolean value;
	
	public JDTBoolean() {
		super(Type.BOOLEAN);
	}
	
	public void setValue(Boolean value) {
		this.value = value;
	}
	
	public Boolean getValue() {
		return this.value;
	}
}
