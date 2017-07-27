package net.pixael.render.data;

public class Texture {
	
	private int id;
	private int width, height;
	
	Texture(int id, int width, int height) {
		this.id = id;
		this.width = width;
		this.height = height;
	}
	
	public int id() {
		return this.id;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public boolean destroy() {
		return GLDataManager.destroy(this);
	}
}
