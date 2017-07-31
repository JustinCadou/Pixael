package net.pixael.render.data

class RawModel protected constructor(val id: Int, val vertexCount: Int) {
	
	fun destroy(): Boolean = GLDataManager.destroy(this)
}