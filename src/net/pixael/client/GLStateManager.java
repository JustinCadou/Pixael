package net.pixael.client;

import org.lwjgl.opengl.GL11;
import net.pixael.render.data.Color;

public class GLStateManager {
	
	public static final State CULL_FACE = new State(GL11.GL_CULL_FACE);
	public static final State DEPTH_TEST = new State(GL11.GL_DEPTH_TEST);
	public static final State BLEND_TEST = new State(GL11.GL_BLEND);
	
	public static void setClearColor(Color color) {
		GL11.glClearColor(color.r, color.g, color.b, color.a);
	}
	
	public static void setToDefaultGLConfiguration() {
		cullFaceMode(CullFace.BACK);
		blendFunc(GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	public static void cullFaceMode(CullFace mode) {
		GL11.glCullFace(mode.id);
	}
	
	public static void blendFunc(int value) {
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, value);
	}
	
	public static enum CullFace {
		FRONT(GL11.GL_FRONT), BACK(GL11.GL_BACK), FRONT_AND_BACK(GL11.GL_FRONT_AND_BACK);
		
		public final int id;
		
		CullFace(int id) {
			this.id = id;
		}
	}
	
	public static class State {
		
		private int id;
		private boolean enabled;
		
		private State(int id) {
			this.id = id;
		}
		
		public void enable() {
			GL11.glEnable(this.id);
			this.enabled = true;
		}
		
		public void disable() {
			GL11.glDisable(this.id);
			this.enabled = false;
		}
		
		public void use(boolean use) {
			if (use) {
				this.enable();
			} else {
				this.disable();
			}
		}
		
		public boolean isEnabled() {
			return this.enabled;
		}
	}
	
	public static class Parameters {
		
	}
}
