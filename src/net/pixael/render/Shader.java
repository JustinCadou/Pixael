package net.pixael.render;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.Scanner;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import net.fantasticfantasy.mainkit.BuffersTool;
import net.fantasticfantasy.mainkit.maths.Matrix4f;
import net.fantasticfantasy.mainkit.maths.Vector2f;
import net.fantasticfantasy.mainkit.maths.Vector3f;
import net.fantasticfantasy.mainkit.maths.Vector4f;
import net.pixael.util.ResourcesUtil;

/**
 * Based on ThinMatrix's (YouTube Channel 
 * <i style="color:blue;text-decoration:underline;">https://www.youtube.com/user/ThinMatrix</i>)
 * .obj file reader code
 */
public abstract class Shader {
	
	private int progID, vertID, fragID;
	
	protected Shader(String vert, String frag) {
		this.progID = GL20.glCreateProgram();
		this.vertID = this.loadShader(vert, GL20.GL_VERTEX_SHADER);
		this.fragID = this.loadShader(frag, GL20.GL_FRAGMENT_SHADER);
		GL20.glAttachShader(this.progID, this.vertID);
		GL20.glAttachShader(this.progID, this.fragID);
		this.bindAttributes();
		GL20.glLinkProgram(this.progID);
		GL20.glValidateProgram(this.progID);
		this.getAllUniformsLocation();
	}
	
	public void destroy() {
		GL20.glDetachShader(this.progID, this.vertID);
		GL20.glDetachShader(this.progID, this.fragID);
		GL20.glDeleteShader(this.vertID);
		GL20.glDeleteShader(this.fragID);
		GL20.glDeleteProgram(this.progID);
	}
	
	public void enable() {
		GL20.glUseProgram(this.progID);
	}
	
	public void disable() {
		GL20.glUseProgram(0);
	}
	
	protected abstract void bindAttributes();
	protected abstract void getAllUniformsLocation();
	
	protected void bindAttribute(int attribNo, String name) {
		GL20.glBindAttribLocation(this.progID, attribNo, name);
	}
	
	protected int getUniformLocation(String name) {
		return GL20.glGetUniformLocation(this.progID, name);
	}
	
	protected void loadInt(int location, int value) {
		GL20.glUniform1i(location, value);
	}
	
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	protected void loadVector2(int location, Vector2f vector) {
		float[] data = new float[2];
		data[0] = vector.x;
		data[1] = vector.y;
		GL20.glUniform2fv(location, data);
	}
	
	protected void loadVector3(int location, Vector3f vector) {
		float[] data = new float[3];
		data[0] = vector.x;
		data[1] = vector.y;
		data[2] = vector.z;
		GL20.glUniform3fv(location, data);
	}
	
	protected void loadVector4(int location, Vector4f vector) {
		float[] data = new float[4];
		data[0] = vector.x;
		data[1] = vector.y;
		data[2] = vector.z;
		data[3] = vector.w;
		GL20.glUniform4fv(location, data);
	}
	
	protected void loadMatrix4(int location, Matrix4f matrix) {
		FloatBuffer buffer = BuffersTool.createFloatBuffer(16);
		matrix.store(buffer);
		buffer.flip();
		GL20.glUniformMatrix4fv(location, false, buffer);
	}
	
	private int loadShader(String name, int type) {
		InputStream in = getShaderSource(name);
		StringBuilder source = new StringBuilder();
		try (Scanner reader = new Scanner(in)) {
			while (reader.hasNextLine()) {
				if (source.length() > 0) {
					source.append("\n");
				}
				source.append(reader.nextLine());
			}
		} catch (Exception e) {
			e.printStackTrace(); //Temporary
		}
		int id = GL20.glCreateShader(type);
		GL20.glShaderSource(id, source);
		GL20.glCompileShader(id);
		if ((GL20.glGetShaderi(id, GL20.GL_COMPILE_STATUS)) == GL11.GL_FALSE) {
			System.out.println("Error in shader " + name);
			System.out.println(GL20.glGetShaderInfoLog(id, 500));
		}
		return id;
	}
	
	private static InputStream getShaderSource(String name) {
		return ResourcesUtil.createAssetInputStream("shaders\\" + name + ".glsl");
	}
}
