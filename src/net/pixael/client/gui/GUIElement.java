package net.pixael.client.gui;

import net.fantasticfantasy.mainkit.NumberUtil;
import net.fantasticfantasy.mainkit.maths.Vector3f;
import net.pixael.Pixael;
import net.pixael.render.data.GLDataManager;
import net.pixael.render.data.RawModel;
import net.pixael.render.data.Texture;
import net.pixael.util.math.Transformation;

public class GUIElement {
	
	public static final RawModel MODEL = createRawModel();
	
	private int x, y, width, height;
	private Texture image;
	private boolean modified;
	private Transformation trans;
	
	public GUIElement(Texture image, int x, int y, int width, int height) {
		this.image = image;
		this.setLocation(x, y);
		this.setDimension(width, height);
	}
	
	public GUIElement(Texture image, int x, int y) {
		this.image = image;
		this.setLocation(x, y);
		this.setDimensionToImageSize();
	}
	
	public GUIElement(Texture image) {
		this.image = image;
		this.setDimensionToImageSize();
	}
	
	public void modify() {
		this.modified = true;
	}
	
	public void setImage(Texture image) {
		this.modified = true;
		this.image = image;
	}
	
	public void setX(int x) {
		this.modified = true;
		this.x = x;
	}
	
	public void setY(int y) {
		this.modified = true;
		this.y = y;
	}
	
	public void setLocation(int x, int y) {
		this.setX(x);
		this.setY(y);
	}
	
	public void setWidth(int width) {
		this.modified = true;
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.modified = true;
		this.height = height;
	}
	
	public void setDimension(int width, int height) {
		this.setWidth(width);
		this.setHeight(height);
	}
	
	public void setDimensionToImageSize() {
		this.setDimension(this.image.getWidth(), this.image.getHeight());
	}
	
	public Transformation getTransformation() {
		if (this.modified) {
			this.trans = new Transformation();
			int[] winSize = Pixael.getPixael().getWindowSize();
			float xscale = 1f / (float) winSize[0];
			float yscale = 1f / (float) winSize[1];
			float xsize = (float) this.width * xscale;
			float ysize = (float) this.height * yscale;
			float xloc = (((float) this.x * (xscale * 2f)) - 1f) + xsize;
			float yloc = (-(((float) this.y * (yscale * 2f)) - 1f)) - ysize;
			this.trans.setScale(new Vector3f(xsize, ysize, 1f));
			this.trans.setTranslation(new Vector3f(xloc, yloc, 0f));
			this.modified = false;
		}
		return this.trans;
	}
	
	public Texture getImage() {
		return this.image;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public boolean isHovered(int mx, int my) {
		return NumberUtil.isBetween(mx, this.x, this.x + this.width) &&
				NumberUtil.isBetween(my, this.y, this.y + this.height);
	}
	
	private static RawModel createRawModel() {
		float[] vertices = new float[] {
			-1, 1, 0,
			-1, -1, 0,
			1, -1, 0,
			1, 1, 0
		};
		float[] texCoords = new float[] {
			0, 0,
			0, 1,
			1, 1,
			1, 0
		};
		float[] normals = new float[] {
			0, 0, 0,
			0, 0, 0,
			0, 0, 0,
			0, 0, 0
		};
		int[] indices = new int[] {
			0, 1, 3,
			3, 1, 2
		};
		return GLDataManager.loadToVAO(vertices, texCoords, normals, indices);
	}
}
