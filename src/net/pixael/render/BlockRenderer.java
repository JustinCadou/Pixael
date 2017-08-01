package net.pixael.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import net.fantasticfantasy.mainkit.maths.Matrix4f;
import net.fantasticfantasy.mainkit.maths.Vector3f;
import net.pixael.Pixael;
import net.pixael.block.Block;
import net.pixael.client.Blocks;
import net.pixael.client.GLStateManager;
import net.pixael.render.data.Color;
import net.pixael.render.data.Light;
import net.pixael.util.math.MatrixUtil;
import net.pixael.util.math.Transformation;
import net.pixael.world.Camera;

public class BlockRenderer {
	
	private BlockShader shader;
	
	public BlockRenderer() {
		this.shader = new BlockShader();
	}
	
	public void render() {
		Pixael pixael = Pixael.getPixael();
		GLStateManager.CULL_FACE.enable();
		GLStateManager.DEPTH_TEST.enable();
		GLStateManager.BLEND_TEST.disable();
		this.shader.enable();
		this.shader.loadViewMatrix(pixael.getPlayer().getView());
		this.shader.loadProjectionMatrix(pixael.getOptions().getProjectionMatrix());
		Transformation trans = new Transformation(new Vector3f(0, 0, -7), new Vector3f(0, 0, 0));
		this.shader.loadTransformationMatrix(trans);
		this.shader.loadSun(new Light(new Vector3f(50, 100, 0), Color.WHITE));
		Block block = Blocks.getBlock("pixael:grass");
		GL30.glBindVertexArray(block.getModel().getId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, block.getTexture().getId());
		GL11.glDrawElements(GL11.GL_TRIANGLES, block.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
		this.shader.disable();
	}
	
	private static class BlockShader extends Shader {
		
		private int loc_transMat,
					loc_projMat,
					loc_viewMat,
					loc_lightPos,
					loc_lightColor;
		
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
			this.loc_lightPos = super.getUniformLocation("lightPos");
			this.loc_lightColor = super.getUniformLocation("lightColor");
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
		
		private void loadSun(Light sun) {
			super.loadVector3(this.loc_lightPos, sun.getPosition());
			super.loadVector3(this.loc_lightColor, sun.getColor3f());
		}
	}
}
