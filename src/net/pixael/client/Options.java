package net.pixael.client;

import net.fantasticfantasy.mainkit.maths.Matrix4f;
import net.pixael.util.math.MatrixUtil;

public class Options {
	
	private Settings settings;
	private Matrix4f projMat;
	
	public Options(Settings settings) {
		this.settings = settings;
	}
	
	public void setFOV(float value) {
		this.settings.set("fov", value);
	}
	
	public void setRenderDistance(int distance) {
		this.settings.set("render_distance", distance);
	}
	
	public float getFOV() {
		return this.settings.getf("fov");
	}
	
	public int getRenderDistance() {
		return this.settings.geti("render_distance");
	}
	
	public Matrix4f getProjectionMatrix() {
		this.createProjectionMatrix();
		return this.projMat;
	}
	
	private void createProjectionMatrix() {
		this.projMat = MatrixUtil.createProjectionMatrix(this.getFOV(), 0.1f, this.getRenderDistance() * 24);
	}
}
