package net.pixael.render;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import net.fantasticfantasy.mainkit.CollectionUtil;
import net.fantasticfantasy.mainkit.maths.Vector2f;
import net.pixael.Pixael;
import net.pixael.client.GLStateManager;
import net.pixael.render.data.Color;
import net.pixael.render.data.GLDataManager;
import net.pixael.render.data.RawModel;

public class GUIShapeDrawer {
	
	private boolean building;
	private Vector2f position;
	private List<Vector2f> points;
	private ShapeShader shader;
	private RawModel model;
	private float[] verts;
	private int[] inds;
	private Color color;
	
	public GUIShapeDrawer() {
		this.position = new Vector2f();
		this.points = new ArrayList<>();
		this.shader = new ShapeShader();
		this.color = Color.BLACK;
	}
	
	public void draw() {
		if (this.model == null) {
			throw new IllegalStateException("Cannot draw with no built shape!");
		}
		GLStateManager.CULL_FACE.disable();
		GLStateManager.DEPTH_TEST.disable();
		GLStateManager.BLEND_TEST.enable();
		this.shader.enable();
		this.shader.loadColor(this.color);
		GL30.glBindVertexArray(this.model.getId());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDrawElements(GL11.GL_TRIANGLES, this.model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		this.shader.disable();
	}
	
	public void start() {
		if (this.building) {
			throw new IllegalStateException("Already building a shape!");
		}
		this.building = true;
		this.points.clear();
		if (this.model != null) {
			GLDataManager.destroy(this.model);
		}
	}
	
	public void end() {
		if (!this.building) {
			throw new IllegalStateException("Not building a shape!");
		}
		this.building = false;
		int[] winDim = Pixael.getPixael().getWindowSize();
		float xScale = 1f / (float) winDim[0];
		float yScale = 1f / (float) winDim[1];
		float[] verts = new float[this.points.size() * 3];
		List<Integer> indices = new ArrayList<>(); 
		int v = 0, i = 0, m = 0, t = 0;
		for (Vector2f point : this.points) {
			verts[v++] = (point.x * xScale) * 2f - 1f;
			verts[v++] = -(point.y * yScale) * 2f + 1f; 
			verts[v++] = 0f;
			indices.add(i++ - m);
			if (++t == 3) {
				m++;
				t = 0;
			}
		}
		this.verts = verts;
		this.inds = CollectionUtil.toIntArray(CollectionUtil.listToArray(indices));
	}
	
	/**
	 * Strongly recommended
	 */
	public void overrideIndices(int[] indices) {
		this.inds = indices;
	}
	
	public void bakeModel() {
		this.model = GLDataManager.loadToVAO(this.verts, new float[0], new float[0], this.inds);
	}
	
	public GUIShapeDrawer position(int x, int y) {
		this.position.setValues(x, y);
		return this;
	}
	
	public GUIShapeDrawer mark() {
		Vector2f mark = new Vector2f();
		mark.load(this.position);
		this.points.add(mark);
		return this;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
	
	public Vector2f position() {
		return this.position;
	}
	
	public Color getColor() {
		return this.color;
	}
	
	private static class ShapeShader extends Shader {
		
		private int loc_color;
		
		public ShapeShader() {
			super("guiShapeVertexShader", "guiShapeFragmentShader");
		}
		
		protected void bindAttributes() {
			super.bindAttribute(0, "vertex");
		}
		
		protected void getAllUniformsLocation() {
			this.loc_color = super.getUniformLocation("color");
		}
		
		private void loadColor(Color color) {
			super.loadVector4(this.loc_color, color.toVector4f());
		}
	}
}
