package net.pixael.jdt;

import java.util.ArrayList;
import java.util.List;

public class JDTObject extends JDTBase {
	
	private static final long serialVersionUID = serialVersionUID();
	
	protected List<JDTBase> value;
	
	public JDTObject() {
		super(Type.OBJECT);
		this.value = new ArrayList<>();
	}
	
	public void addTag(JDTBase value) {
		this.value.add(value);
	}
	
	public JDTBase getTag(String name) {
		for (int i = 0; i < this.value.size(); i++) {
			if (this.value.get(i).name.equals(name)) {
				return this.value.get(i);
			}
		}
		return null;
	}
	
	public void removeTag(JDTBase value) {
		this.value.remove(value);
	}
	
	public List<JDTBase> getValue() {
		return this.value;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (this.name != null) {
			sb.append(this.name);
			sb.append(":");
		}
		sb.append("{");
		for (JDTBase jdt : this.value) {
			sb.append(jdt.toString());
			sb.append(",");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("}");
		return sb.toString();
	}
}
