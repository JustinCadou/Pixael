package net.pixael.jdt;

public class JDTLong extends JDTBase {
	
	private static final long serialVersionUID = serialVersionUID();
	
	protected Long value;
	
	public JDTLong() {
		super(Type.LONG);
	}
	
	public void setValue(Long value) {
		this.value = value;
	}
	
	public Long getValue() {
		return this.value;
	}
	
	public String toString() {
		return super.toString() + "l";
	}
}
