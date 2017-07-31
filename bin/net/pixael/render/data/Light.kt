package net.pixael.render.data

import net.fantasticfantasy.mainkit.maths.Vector3f

class Light(var position: Vector3f = Vector3f(), var color: Color = Color.WHITE) {
	
	fun move(position: Vector3f) {
		this.position.add(position)
	}
	
	fun getColor3f(): Vector3f = this.color.toVector3f()
}