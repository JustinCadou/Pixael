package net.pixael.render.data

class Texture protected constructor(val id: Int, val width: Int, val height: Int) {
	
	fun destroy(): Boolean = GLDataManager.destroy(this)
}