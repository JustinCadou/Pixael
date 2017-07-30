package net.pixael.util.math;

import net.fantasticfantasy.mainkit.maths.Vector3f;

public class Transformation {
	
	public static final int X = 1 << 0,
							Y = 1 << 1,
							Z = 2 << 2;
	
	private Vector3f translation, rotation, scale;
	
	public Transformation(Vector3f translation, Vector3f rotation, Vector3f scale) {
		this.translation = translation;
		this.rotation = rotation;
		this.scale = scale;
	}
	
	public Transformation(Vector3f translation, Vector3f rotation) {
		this.translation = translation;
		this.rotation = rotation;
		this.scale = new Vector3f(1, 1, 1);
	}
	
	public Transformation(Vector3f translation) {
		this.translation = translation;
		this.rotation = new Vector3f(0, 0, 0);
		this.scale = new Vector3f(1, 1, 1);
	}
	
	public Transformation() {
		this.translation = new Vector3f(0, 0, 0);
		this.rotation = new Vector3f(0, 0, 0);
		this.scale = new Vector3f(1, 1, 1);
	}
	
	public Transformation move(Vector3f translation, int rotationAxis) {
//		boolean rotxax = (X & rotationAxis) == X;
		boolean rotyax = (Y & rotationAxis) == Y;
//		boolean rotzax = (Z & rotationAxis) == Z;
		float cosYZ = (float) Math.cos(Math.toRadians(this.rotation.y));
		float sinYZ = (float) Math.sin(Math.toRadians(this.rotation.y));
		float cosYX = (float) Math.cos(Math.toRadians(this.rotation.y - 90));
		float sinYX = (float) Math.sin(Math.toRadians(this.rotation.y - 90));
//		float cosXY = (float) Math.cos(Math.toRadians(this.rotation.x));
//		float sinXY = (float) Math.sin(Math.toRadians(this.rotation.x));
//		float cosXZ = (float) Math.cos(Math.toRadians(this.rotation.x - 90));
//		float sinXZ = (float) Math.sin(Math.toRadians(this.rotation.x - 90));
//		float cosZX = (float) Math.cos(Math.toRadians(this.rotation.z));
//		float sinZX = (float) Math.sin(Math.toRadians(this.rotation.z));
//		float cosZY = (float) Math.cos(Math.toRadians(this.rotation.z - 90));
//		float sinZY = (float) Math.sin(Math.toRadians(this.rotation.z - 90));
		Vector3f trans;
		if (rotyax) {
			trans = new Vector3f();
			trans.x = translation.z * -sinYZ;
			trans.z = translation.z * cosYZ;
			this.translate(trans);
			trans = new Vector3f();
			trans.x = translation.x * sinYX;
			trans.z = translation.x * -cosYX;
			this.translate(trans);
			this.translate(new Vector3f(0, translation.y, 0));
		}
		return this;
	}
	
	public Transformation translate(Vector3f translation) {
		this.translation.add(translation);
		return this;
	}
	
	public Transformation rotate(Vector3f rotation) {
		this.rotation.add(rotation);
		return this;
	}
	
	public Transformation scale(Vector3f scale) {
		this.scale.mul(scale);
		return this;
	}
	
	public void setTranslation(Vector3f translation) {
		this.translation = translation;
	}
	
	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	
	public void setScale(Vector3f scale) {
		this.scale = scale;
	}
	
	public Vector3f getTranslation() {
		return this.translation;
	}
	
	public Vector3f getRotation() {
		return this.rotation;
	}
	
	public Vector3f getScale() {
		return this.scale;
	}
}
