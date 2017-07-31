package net.pixael.world

import net.fantasticfantasy.mainkit.maths.Vector3f

class Camera(var position: Vector3f = Vector3f(), var rotation: Vector3f = Vector3f()) {
	
	fun translate(translation: Vector3f) {
		this.position.add(translation)
	}
	
	fun rotate(rotation: Vector3f) {
		this.rotation.add(rotation)
	}
}