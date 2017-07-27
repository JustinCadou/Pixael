package net.pixael.jdt;

import java.util.ArrayList;
import java.util.List;

public class JDTList extends JDTBase {
	
	private static final long serialVersionUID = serialVersionUID();
	
	protected List<Object> value;
	protected Type valueType;
	
	public JDTList(Type valueType) {
		super(Type.LIST);
		this.valueType = valueType;
		this.value = new ArrayList<>();
	}
	
	public void addValue(Object value) {
		this.value.add(value);
	}
	
	public void removeValue(Object value) {
		this.value.remove(value);
	}
	
	public int size() {
		return this.value.size();
	}
	
	public Object getValue(int value) {
		return this.value.get(value);
	}
	
	public List<Object> getValue() {
		return this.value;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.name);
		sb.append(":[");
		for (Object value : this.value) {
			if (sb.length() > 1) {
				sb.append(",");
			}
			sb.append(value.toString());
		}
		sb.append("]");
		return sb.toString();
	}
}
