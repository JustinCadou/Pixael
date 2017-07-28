package net.pixael.entity;

import net.pixael.util.math.Transformation;

public class Entity {
	
	protected String name;
	protected Transformation transformation;
	
	protected Entity() {
		this.transformation = new Transformation();
	}
	
	public String getName() {
		return this.name;
	}
	
	public Transformation getTransformation() {
		return this.transformation;
	}
}
