package net.pixael.jdt;

public class JDTDouble extends JDTBase {
	
	private static final long serialVersionUID = serialVersionUID();
	
	protected Double value;
	
	public JDTDouble() {
		super(Type.DOUBLE);
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
	
	public Double getValue() {
		return this.value;
	}
	
	public String toString() {
		return super.toString() + "d";
	}
}
