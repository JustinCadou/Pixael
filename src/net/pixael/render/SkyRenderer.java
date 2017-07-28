package net.pixael.render;

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
import net.pixael.util.math.MatrixUtil;
import net.pixael.util.math.Transformation;
import net.pixael.world.Camera;

public class SkyRenderer {
	
	private StarShader starShader;
	private Transformation starsTrans;
	
	public SkyRenderer() {
		this.starShader = new StarShader();
		this.starsTrans = new Transformation(new Vector3f(0, 100f, 0), new Vector3f(105, 0, 0), new Vector3f(100, 100, 1));
	}
	
	public void render(int... time) {
		GLStateManager.CULL_FACE.disable();
		GLStateManager.DEPTH_TEST.disable();
		this.starShader.enable();
		Pixael pixael = Pixael.getPixael();
		this.starShader.loadTransformationMatrix(this.starsTrans);
		this.starShader.loadRotationMatrix(pixael.getPlayer().getView());
		this.starShader.loadProjectionMatrix(pixael.getOptions().getProjectionMatrix());
		RawModel model = GUIElement.MODEL;
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
