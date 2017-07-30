package net.pixael.entity;

import net.pixael.util.math.Transformation;

public abstract class Entity {
	
	protected String name;
	protected Transformation transformation;
	
	protected Entity() {
		this.transformation = new Transformation();
	}
	
	public abstract void tick();
	
	public String getName() {
		return this.name;
	}
	
	public Transformation getTransformation() {
		return this.transformation;
	}
}
