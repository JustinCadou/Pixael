package net.pixael.render.data;

import net.fantasticfantasy.mainkit.maths.Vector3f;
import net.fantasticfantasy.mainkit.maths.Vector4f;

public class Color {
	
	public static final Color RED = new Color(1, 0, 0);
	public static final Color GREEN = new Color(0, 1, 0);
	public static final Color BLUE = new Color(0, 0, 1);
	public static final Color YELLOW = new Color(1, 1, 0);
	public static final Color PURPLE = new Color(1, 0, 1);
	public static final Color AQUA = new Color(0, 1, 1);
	public static final Color PINK = new Color(1, 0, 0.5f);
	public static final Color ORANGE = new Color(1, 0.5f, 0);
	public static final Color BLACK = new Color(0);
	public static final Color WHITE = new Color(1);
	public static final Color GRAY = new Color(0.5f);
	public static final Color DARK_GRAY = new Color(0.25f);
	public static final Color LIGHT_GRAY = new Color(0.75f);
	public static final Color VOID = new Color(0, 0);
	
	public float r, g, b, a;
	
	public Color() {
		this.a = 1f;
	}
	
	public Color(float gs) {
		this.setColor(gs, gs, gs, 1f);
	}
	
	public Color(float gs, float a) {
		this.setColor(gs, gs, gs, a);
	}
	
	public Color(float r, float g, float b) {
		this.setColor(r, g, b, 1f);
	}
	
	public Color(float r, float g, float b, float a) {
		this.setColor(r, g, b, a);
	}
	
	public void setColor(float r, float g, float b, float a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
	public Vector3f toVector3f() {
		return new Vector3f(this.r, this.g, this.b);
	}
	
	public Vector4f toVector4f() {
		return new Vector4f(this.r, this.g, this.b, this.a);
	}
	
	public static float hexToFloat(int c) {
		return (float) c / 255f;
	}
	
	public static int floatToHex(float c) {
		return (int) (c * 255f);
	}
	
	public static Color hexColor(int r, int g, int b, int a) {
		return new Color(hexToFloat(r), hexToFloat(g), hexToFloat(b), hexToFloat(a));
	}
}
