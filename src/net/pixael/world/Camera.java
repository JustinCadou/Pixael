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
		this.position.add(translation);
	}
	
	public void rotate(Vector3f rotation) {
		this.rotation.add(rotation);
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
