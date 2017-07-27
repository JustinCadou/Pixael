package net.pixael.jdt;

public class JDTByte extends JDTBase {

	private static final long serialVersionUID = serialVersionUID();
	
	protected Byte value;
	
	public JDTByte() {
		super(Type.BYTE);
	}
	
	public void setValue(Byte value) {
		this.value = value;
	}
	
	public Byte getValue() {
		return this.value;
	}
	
	public String toString() {
		return super.toString() + "b";
	}
}
