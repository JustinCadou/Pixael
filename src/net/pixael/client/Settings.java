package net.pixael.client;

import java.util.HashMap;
import java.util.Map;
import net.fantasticfantasy.mainkit.maths.Matrix4f;
import net.fantasticfantasy.mainkit.maths.Vector2f;
import net.fantasticfantasy.mainkit.maths.Vector3f;
import net.fantasticfantasy.mainkit.maths.Vector4f;

public class Settings {
	
	private Map<String, Object> settings;
	
	public Settings() {
		this.settings = new HashMap<>();
	}
	
	public void put(String id, Object value) {
		this.settings.put(id, value);
	}
	
	protected void set(String id, Object value) {
		this.settings.replace(id, value);
	}
	
	protected Object remove(String id) {
		return this.settings.remove(id);
	}
	
	public Object get(String id) {
		return this.settings.get(id);
	}
	
	public Byte getb(String id) {
		return (Byte) this.settings.get(id);
	}
	
	public Short gets(String id) {
		return (Short) this.settings.get(id);
	}
	
	public Integer geti(String id) {
		return (Integer) this.settings.get(id);
	}
	
	public Long getl(String id) {
		return (Long) this.settings.get(id);
	}
	
	public Float getf(String id) {
		return (float) (double) this.getd(id);
	}
	
	public Double getd(String id) {
		return (Double) this.settings.get(id);
	}
	
	public Character getc(String id) {
		return (Character) this.settings.get(id);
	}
	
	public Boolean getbl(String id) {
		return (Boolean) this.settings.get(id);
	}
	
	public String getstr(String id) {
		return (String) this.settings.get(id);
	}
	
	public Vector2f getv2f(String id) {
		return (Vector2f) this.settings.get(id);
	}
	
	public Vector3f getv3f(String id) {
		return (Vector3f) this.settings.get(id);
	}
	
	public Vector4f getv4f(String id) {
		return (Vector4f) this.settings.get(id);
	}
	
	public Matrix4f getm4f(String id) {
		return (Matrix4f) this.settings.get(id);
	}
	
	public Map<String, Object> map() {
		return this.settings;
	}
}