package net.pixael;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Keyboard extends GLFWKeyCallback {
	
	public static final int FUNCTION_KEY = 65536;
	
	private static final Keyboard I = new Keyboard();
	private boolean[] keys;
	
	private Keyboard() {
		this.keys = new boolean[65537];
	}
	
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if (key < 0) {
			key = FUNCTION_KEY;
		}
		this.keys[key] = action != GLFW.GLFW_RELEASE;
	}
	
	public static boolean isAnyKeyDown() {
		for (int i = 0; i < I.keys.length; i++) {
			if (I.keys[i]) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isKeyDown(int key) {
		return I.keys[key];
	}
	
	static Keyboard getKeyboard() {
		return I;
	}
}
