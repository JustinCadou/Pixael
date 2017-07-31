package net.pixael.render;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import net.fantasticfantasy.mainkit.maths.Matrix4f;
import net.pixael.client.GLStateManager;
import net.pixael.client.gui.GUIElement;
import net.pixael.util.math.MatrixUtil;
import net.pixael.util.math.Transformation;

public class GUIRenderer {
	
	private GUIShader shader;
	private List<GUIElement> elements;
	
	public GUIRenderer() {
		this.shader = new GUIShader();
		this.elements = new ArrayList<>();
	}
	
	public void processElement(GUIElement element) {
		this.elements.add(element);
	}
	
	public void render() {
		GLStateManager.DEPTH_TEST.disable();
		GLStateManager.BLEND_TEST.enable();
		this.shader.enable();
		GL30.glBindVertexArray(GUIElement.MODEL.getId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		for (GUIElement element : this.elements) {
			this.shader.loadTransformation(element.getTransformation());
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, element.getImage().getId());
			GL11.glDrawElements(GL11.GL_TRIANGLES, GUIElement.MODEL.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		}
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
		this.shader.disable();
		this.elements.clear();
	}
	
	public void cleanUp() {
		this.shader.destroy();
	}
	
	private static class GUIShader extends Shader {
		
		private int loc_transMat;
		
		private GUIShader() {
			super("guiVertexShader", "guiFragmentShader");
		}
		
		protected void bindAttributes() {
			super.bindAttribute(0, "vertex");
			super.bindAttribute(1, "texCoords");
		}
		
		protected void getAllUniformsLocation() {
			this.loc_transMat = super.getUniformLocation("transMat");
		}
		
		public void loadTransformation(Transformation trans) {
			Matrix4f mat = MatrixUtil.createTransformationMatrix(trans);
			super.loadMatrix4(this.loc_transMat, mat);
		}
	}
}
