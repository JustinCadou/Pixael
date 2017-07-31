package net.pixael.render.data;

import net.fantasticfantasy.mainkit.maths.Vector3f;

public class Light {
	
	private Vector3f position;
	private Color color;
	
	public Light(Vector3f position) {
		this.position = position;
		this.color = Color.WHITE;
	}
	
	public Light(Color color) {
		this.position = new Vector3f();
		this.color = color;
	}
	
	public Light(Vector3f position, Color color) {
		this.position = position;
		this.color = color;
	}
	
	public Light() {
		this.position = new Vector3f();
		this.color = Color.WHITE;
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public void move(Vector3f translation) {
		this.position.add(translation);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
	
	public Vector3f getColor3f() {
		return this.color.toVector3f();
	}
	
	public Color getColor() {
		return this.color;
	}
}
