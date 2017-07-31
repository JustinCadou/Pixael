package net.pixael.render;

import java.io.InputStream;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import net.fantasticfantasy.mainkit.maths.Matrix4f;
import net.fantasticfantasy.mainkit.maths.Vector3f;
import net.pixael.Pixael;
import net.pixael.client.GLStateManager;
import net.pixael.client.Textures;
import net.pixael.client.gui.GUIElement;
import net.pixael.render.data.RawModel;
import net.pixael.util.OBJFileReader;
import net.pixael.util.ResourcesUtil;
import net.pixael.util.math.MatrixUtil;
import net.pixael.util.math.Transformation;
import net.pixael.world.Camera;

public class SkyRenderer {
	
	private SkyColorShader skyShader;
	private Transformation skyTrans;
	private StarShader starShader;
	private Transformation starsTrans;
	private RawModel box;
	
	public SkyRenderer() {
		this.skyShader = new SkyColorShader();
		this.skyTrans = new Transformation(new Vector3f(-6, -6, -6), new Vector3f(90, 0, 0), new Vector3f(12, 12, 12));
		this.starShader = new StarShader();
		this.starsTrans = new Transformation(new Vector3f(0, 12, 0), new Vector3f(90, 0, 0), new Vector3f(25, 25, 1));
		InputStream in = ResourcesUtil.createAssetInputStream("full_block.obj");
		this.box = OBJFileReader.loadOBJFile(in);
	}
	
	public void render(int... time) {
		GLStateManager.CULL_FACE.disable();
		GLStateManager.DEPTH_TEST.disable();
		GLStateManager.BLEND_TEST.disable();
		Pixael pixael = Pixael.getPixael();
		float fov = pixael.getOptions().getFOV();
		Matrix4f projMat = MatrixUtil.createProjectionMatrix(fov, 0.1f, 36f);
		this.skyShader.enable();
		this.skyShader.loadTransformationMatrix(this.skyTrans);
		this.skyShader.loadRotationMatrix(pixael.getPlayer().getView());
		this.skyShader.loadProjectionMatrix(projMat);
		GL30.glBindVertexArray(this.box.getID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, this.box.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		this.skyShader.disable();
		RawModel model = GUIElement.MODEL;
		this.starShader.enable();
		this.starShader.loadTransformationMatrix(this.starsTrans);
		this.starShader.loadRotationMatrix(pixael.getPlayer().getView());
		this.starShader.loadProjectionMatrix(projMat);
		GL30.glBindVertexArray(model.getID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, Textures.get("pixael:world_sky_star_map").id());
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		this.starShader.disable();
	}
	
	public void cleanUp() {
		this.skyShader.destroy();
		this.starShader.destroy();
	}
	
	private static class SkyColorShader extends Shader {
		
		private int loc_transMat;
		private int loc_rotMat;
		private int loc_projMat;
		
		private SkyColorShader() {
			super("skyColorVertexShader", "skyColorFragmentShader");
		}
		
		protected void bindAttributes() {
			super.bindAttribute(0, "vertex");
		}
		
		protected void getAllUniformsLocation() {
			this.loc_transMat = super.getUniformLocation("transMat");
			this.loc_rotMat = super.getUniformLocation("rotMat");
			this.loc_projMat = super.getUniformLocation("projMat");
		}
		
		private void loadRotationMatrix(Camera cam) {
			Camera nCam = new Camera(new Vector3f(), cam.getRotation());
			Matrix4f rotMat = MatrixUtil.createViewMatrix(nCam);
			super.loadMatrix4(this.loc_rotMat, rotMat);
		}
		
		private void loadProjectionMatrix(Matrix4f matrix) {
			super.loadMatrix4(this.loc_projMat, matrix);
		}
		
		private void loadTransformationMatrix(Transformation trans) {
			Matrix4f transMat = MatrixUtil.createTransformationMatrix(trans);
			super.loadMatrix4(this.loc_transMat, transMat);
		}
	}
	
	private static class StarShader extends Shader {
		
		private int loc_transMat;
		private int loc_rotMat;
		private int loc_projMat;
		
		private StarShader() {
			super("skyStarsVertexShader", "skyStarsFragmentShader");
		}
		
		protected void bindAttributes() {
			super.bindAttribute(0, "vertex");
			super.bindAttribute(1, "texCoords");
		}
		
		protected void getAllUniformsLocation() {
			this.loc_transMat = super.getUniformLocation("transMat");
			this.loc_rotMat = super.getUniformLocation("rotMat");
			this.loc_projMat = super.getUniformLocation("projMat");
		}
		
		private void loadRotationMatrix(Camera cam) {
			Camera nCam = new Camera(new Vector3f(), cam.getRotation());
			Matrix4f rotMat = MatrixUtil.createViewMatrix(nCam);
			super.loadMatrix4(this.loc_rotMat, rotMat);
		}
		
		private void loadProjectionMatrix(Matrix4f matrix) {
			super.loadMatrix4(this.loc_projMat, matrix);
		}
		
		private void loadTransformationMatrix(Transformation trans) {
			Matrix4f transMat = MatrixUtil.createTransformationMatrix(trans);
			super.loadMatrix4(this.loc_transMat, transMat);
		}
	}
}
