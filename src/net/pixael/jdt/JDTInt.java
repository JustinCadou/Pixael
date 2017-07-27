package net.pixael.jdt;

public class JDTInt extends JDTBase {
	
	private static final long serialVersionUID = serialVersionUID();
	
	protected Integer value;
	
	public JDTInt() {
		super(Type.INT);
	}
	
	public void setValue(Integer value) {
		this.value = value;
	}
	
	public Integer getValue() {
		return this.value;
	}
}
