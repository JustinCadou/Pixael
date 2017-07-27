package net.pixael;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class Mouse extends GLFWMouseButtonCallback {
	
	private static final Mouse I = new Mouse();
	private boolean buttons[];
	
	public static final int LEFT_BUTTON = GLFW.GLFW_MOUSE_BUTTON_LEFT;
	public static final int RIGHT_BUTTON = GLFW.GLFW_MOUSE_BUTTON_RIGHT;
	public static final int MIDDLE_BUTTON = GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
	
	private Mouse() {
		this.buttons = new boolean[17];
	}
	
	public void invoke(long window, int button, int action, int mods) {
		if (button < 0) {
			button = 16;
		}
		this.buttons[button] = action != GLFW.GLFW_RELEASE;
	}
	
	public static boolean isAnyButtonDown() {
		for (int i = 0; i < I.buttons.length; i++) {
			if (I.buttons[i]) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isButtonDown(int button) {
		return I.buttons[button];
	}
	
	static Mouse getInstance() {
		return I;
	}
}
