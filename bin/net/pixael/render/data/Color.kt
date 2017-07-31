package net.pixael.render.data

import net.fantasticfantasy.mainkit.maths.Vector3f
import net.fantasticfantasy.mainkit.maths.Vector4f

class Color(var r: Float = 0.0f, var g: Float = 0.0f, var b: Float = 0.0f, var a: Float = 1.0f) {
	
	companion object {
		@JvmField val RED: Color = Color(1.0f, 0.0f, 0.0f)
		@JvmField val GREEN: Color = Color(0.0f, 1.0f, 0.0f)
		@JvmField val BLUE: Color = Color(0.0f, 0.0f, 1.0f)
		@JvmField val YELLOW: Color = Color(1.0f, 1.0f, 0.0f)
		@JvmField val PURPLE: Color = Color(1.0f, 0.0f, 1.0f)
		@JvmField val CYAN: Color = Color(0.0f, 1.0f, 1.0f)
		@JvmField val ORANGE: Color = Color(1.0f, 0.5f, 0.0f)
		@JvmField val PINK: Color = Color(1.0f, 0.0f, 0.5f)
		@JvmField val BLACK: Color = Color()
		@JvmField val WHITE: Color = Color(1.0f, 1.0f, 1.0f)
		@JvmField val GRAY: Color = Color(0.5f, 0.5f, 0.5f)
		@JvmField val VOID: Color = Color(0.0f, 0.0f, 0.0f, 0.0f)
	}
	
	fun setValues(r: Float, g: Float, b: Float, a: Float) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	fun toVector3f(): Vector3f = Vector3f(this.r, this.g, this.b);
	
	fun toVector4f(): Vector4f = Vector4f(this.r, this.g, this.b, this.a);
}