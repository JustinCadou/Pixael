package net.pixael.render.data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import net.fantasticfantasy.mainkit.BuffersTool;
import net.fantasticfantasy.mainkit.CollectionUtil;

public class GLDataManager {
	
	private static Map<RawModel, List<Integer>> models;
	private static RawModel curr;
	private static List<Texture> textures;
	
	public static void init() {
		models = new HashMap<>();
		textures = new ArrayList<>();
	}
	
	public static RawModel loadToVAO(float[] vertices, float[] texCoords, float[] normals, int[] indices) {
		int vao = GL30.glGenVertexArrays();
		RawModel model = new RawModel(vao, indices.length);
		List<Integer> vbos = new ArrayList<>();
		models.put(model, vbos);
		curr = model;
		GL30.glBindVertexArray(vao);
		createIndicesVBO(indices);
		createVBO(0, 3, vertices);
		createVBO(1, 2, texCoords);
		createVBO(2, 3, normals);
		GL30.glBindVertexArray(0);
		curr = null;
		return model;
	}
	
	private static void createIndicesVBO(int[] indices) {
		int vbo = GL15.glGenBuffers();
		models.get(curr).add(vbo);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
		IntBuffer buffer = BuffersTool.createIntBuffer(indices.length);
		CollectionUtil.store(CollectionUtil.toIntObjArray(indices), buffer);
		buffer.flip();
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private static void createVBO(int attribNo, int dataLength, float[] data) {
		int vbo = GL15.glGenBuffers();
		models.get(curr).add(vbo);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		FloatBuffer buffer = BuffersTool.createFloatBuffer(data.length);
		CollectionUtil.store(CollectionUtil.toFloatObjArray(data), buffer);
		buffer.flip();
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attribNo, dataLength, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	public static Texture loadTexture(InputStream in, boolean flat) throws IOException {
		PNGDecoder png = new PNGDecoder(in);
		ByteBuffer pxls = BuffersTool.createByteBuffer(png.getWidth() * png.getHeight() * 4);
		png.decode(pxls, png.getWidth() * 4, Format.RGBA);
		pxls.flip();
		int id = GL11.glGenTextures();
		Texture tex = new Texture(id, png.getWidth(), png.getHeight());
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_RESCALE_NORMAL);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_RESCALE_NORMAL);
		int mfilter = flat ? GL11.GL_NEAREST : GL11.GL_LINEAR;
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, mfilter);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, mfilter);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, png.getWidth(), png.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, pxls);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		return tex;
	}
	
	public static boolean destroy(RawModel model) {
		boolean r = models.containsKey(model);
		if (!r) {
			return false;
		}
		List<Integer> vbos = models.remove(model);
		GL30.glDeleteVertexArrays(model.getId());
		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		return true;
	}
	
	public static boolean destroy(Texture texture) {
		boolean r = textures.contains(texture);
		if (!r) {
			return false;
		}
		GL11.glDeleteTextures(texture.getId());
		textures.remove(texture);
		return true;
	}
	
	public static void close() {
		List<RawModel> modelList = CollectionUtil.mapKeysToList(models);
		for (int i = 0; i < models.size(); i++) {
			modelList.get(i).destroy();
		}
		for (int i = 0; i < textures.size(); i++) {
			textures.get(i).destroy();
		}
	}
}
