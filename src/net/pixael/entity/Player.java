package net.pixael.entity;

import net.fantasticfantasy.mainkit.maths.Vector3f;
import net.pixael.util.math.Transformation;
import net.pixael.world.Camera;

public class Player extends Entity {
	
	protected Camera view;
	
	public Player(String username) {
		super();
		this.view = new Camera(this.transformation.getTranslation(), this.transformation.getRotation());
	}
	
	public void tick() {
		
	}
	
	public void rotate(Vector3f rotation) {
		Vector3f rot = this.transformation.getRotation();
		rot.add(rotation);
		if (rot.x > 90) {
			rot.x = 90;
		} else if (rot.x < -90) {
			rot.x = -90;
		}
	}
	
	public void move(Vector3f move) {
		this.transformation.move(move, Transformation.Y);
	}
	
	public Camera getView() {
		return this.view;
	}
}
