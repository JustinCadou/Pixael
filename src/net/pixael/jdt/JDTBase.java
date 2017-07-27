package net.pixael.jdt;

import java.io.Serializable;

public abstract class JDTBase implements Serializable {
	
	private static final long serialVersionUID = serialVersionUID();
	
	private static int count;
	
	public static enum Type {
		BYTE, SHORT, INT, LONG, DOUBLE, BOOLEAN, STRING, LIST, OBJECT;
		
		private int id;
		
		Type() {
			this.id = count++;
		}
		
		public int getID() {
			return this.id;
		}
	}
	
	protected Type type;
	protected String name;
	
	protected JDTBase(Type type) {
		this.type = type;
	}
	
	public final void setName(String name) {
		this.name = name;
	}
	
	public final String getName() {
		return this.name;
	}
	
	public final Type getType() {
		return this.type;
	}
	
	public abstract Object getValue();
	
	public JDTBoolean toJDTBoolean() {
		JDTBoolean jdt = new JDTBoolean();
		jdt.name = this.name;
		return jdt;
	}
	
	public JDTByte toJDTByte() {
		JDTByte jdt = new JDTByte();
		jdt.name = this.name;
		return jdt;
	}
	
	public JDTDouble toJDTDouble() {
		JDTDouble jdt = new JDTDouble();
		jdt.name = this.name;
		return jdt;
	}
	
	public String toString() {
		return this.name + ":" + this.getValue();
	}
	
	protected static final long serialVersionUID() {
		return 1001L;
	}
	
	public static JDTBase create(Type type, Object... parameters) {
		switch (type) {
		case BOOLEAN:
			return new JDTBoolean();
		case BYTE:
			return new JDTByte();
		case DOUBLE:
			return new JDTDouble();
		case INT:
			return new JDTInt();
		case LIST:
			return new JDTList((Type) parameters[0]);
		case LONG:
			return new JDTLong();
		case OBJECT:
			return new JDTObject();
		case SHORT:
			return new JDTShort();
		case STRING:
			return new JDTString();
		}
		return null;
	}
}
