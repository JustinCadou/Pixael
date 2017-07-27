package net.pixael.render.data;

public class RawModel {
	
	private int id;
	private int vertCount;
	
	RawModel(int id, int vertCount) {
		this.id = id;
		this.vertCount = vertCount;
	}
	
	public int getID() {
		return this.id;
	}
	
	public int getVertexCount() {
		return this.vertCount;
	}
	
	public boolean destroy() {
		return GLDataManager.destroy(this);
	}
}
