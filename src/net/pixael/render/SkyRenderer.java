package net.pixael.render;

import net.fantasticfantasy.mainkit.maths.Matrix4f;
import net.pixael.util.math.MatrixUtil;
import net.pixael.world.Camera;

public class SkyRenderer {
	
	private StarShader starShader;
	
	public SkyRenderer() {
		this.starShader = new StarShader();
	}
	
	public void render(int time) {
		this.starShader.enable();
		this.starShader.disable();
	}
	
	private static class StarShader extends Shader {
		
		private int loc_rotMat;
		
		private StarShader() {
			super("skyStarsVertexShader", "skyStarsFragmentShader");
		}
		
		protected void bindAttributes() {
			super.bindAttribute(0, "vertex");
			super.bindAttribute(1, "texCoords");
		}
		
		protected void getAllUniformsLocation() {
			this.loc_rotMat = super.getUniformLocation("rotMat");
		}
		
		private void loadRotationMatrix(Camera cam) {
			Camera nCam = new Camera(cam.getPosition());
			Matrix4f rotMat = MatrixUtil.createViewMatrix(nCam);
			super.loadMatrix4(this.loc_rotMat, rotMat);
		}
	}
}
