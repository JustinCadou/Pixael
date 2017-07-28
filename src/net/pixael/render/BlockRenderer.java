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
import net.pixael.render.data.RawModel;
import net.pixael.util.OBJFileReader;
import net.pixael.util.ResourcesUtil;
import net.pixael.util.math.MatrixUtil;
import net.pixael.util.math.Transformation;
import net.pixael.world.Camera;

public class BlockRenderer {
	
	private BlockShader shader;
	
	public BlockRenderer() {
		this.shader = new BlockShader();
		_temp_();
	}
	
	public void render() {
		Pixael pixael = Pixael.getPixael();
		GLStateManager.CULL_FACE.enable();
		GLStateManager.DEPTH_TEST.enable();
		GLStateManager.BLEND_TEST.disable();
		this.shader.enable();
		this.shader.loadViewMatrix(pixael.getPlayer().getView());
		this.shader.loadProjectionMatrix(pixael.getOptions().getProjectionMatrix());
		rotation += 0.005f;
		Transformation trans = new Transformation(new Vector3f(0, 0, -7), new Vector3f(0, rotation, 0));
		this.shader.loadTransformationMatrix(trans);
		GL30.glBindVertexArray(_temp_vao.getID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 3);
		GL11.glDrawElements(GL11.GL_TRIANGLES, _temp_vao.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		this.shader.disable();
	}
	
	private static RawModel _temp_vao;
	private static float rotation;
	
	private static void _temp_() {
		InputStream in = ResourcesUtil.createAssetInputStream("full_block.obj");
		_temp_vao = OBJFileReader.loadOBJFile(in);
	}
	
	private static class BlockShader extends Shader {
		
		private int loc_transMat,
					loc_projMat,
					loc_viewMat;
		
		private BlockShader() {
			super("blockVertexShader", "blockFragmentShader");
		}
		
		protected void bindAttributes() {
			super.bindAttribute(0, "vertex");
			super.bindAttribute(1, "texCoords");
			super.bindAttribute(2, "normal");
		}
		
		protected void getAllUniformsLocation() {
			this.loc_transMat = super.getUniformLocation("transMat");
			this.loc_projMat = super.getUniformLocation("projMat");
			this.loc_viewMat = super.getUniformLocation("viewMat");
		}
		
		private void loadTransformationMatrix(Transformation trans) {
			Matrix4f transMat = MatrixUtil.createTransformationMatrix(trans);
			super.loadMatrix4(this.loc_transMat, transMat);
		}
		
		private void loadProjectionMatrix(Matrix4f matrix) {
			super.loadMatrix4(this.loc_projMat, matrix);
		}
		
		private void loadViewMatrix(Camera cam) {
			Matrix4f viewMat = MatrixUtil.createViewMatrix(cam);
			super.loadMatrix4(this.loc_viewMat, viewMat);
		}
	}
}
