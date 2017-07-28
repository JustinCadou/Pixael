package net.pixael.entity;

import net.pixael.world.Camera;

public class Player extends Entity {
	
	protected Camera view;
	
	public Player(String username) {
		super();
		this.view = new Camera();
	}
	
	public Camera getView() {
		return this.view;
	}
}
