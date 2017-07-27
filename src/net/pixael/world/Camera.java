package net.pixael.world;

import net.fantasticfantasy.mainkit.maths.Vector3f;

public class Camera {
	
	private Vector3f position, rotation;
	
	public Camera(Vector3f position, Vector3f rotation) {
		this.position = position;
		this.rotation = rotation;
	}
	
	public Camera(Vector3f position) {
		this.position = position;
		this.rotation = new Vector3f();
	}
	
	public Camera() {
		this.position = new Vector3f();
		this.rotation = new Vector3f();
	}
	
	public void translate(Vector3f translation) {
		Vector3f.add(this.position, translation, this.position);
	}
	
	public void rotate(Vector3f rotation) {
		Vector3f.add(this.position, rotation, this.position);
	}
	
	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	
	public Vector3f getPosition() {
		return this.position;
	}
	
	public Vector3f getRotation() {
		return this.rotation;
	}
}
