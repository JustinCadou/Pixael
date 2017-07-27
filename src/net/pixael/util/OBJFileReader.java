package net.pixael.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import net.fantasticfantasy.mainkit.maths.Vector2f;
import net.fantasticfantasy.mainkit.maths.Vector3f;
import net.pixael.render.data.GLDataManager;
import net.pixael.render.data.RawModel;

/**
 * Based on ThinMatrix's (YouTube Channel 
 * <i style="color:blue;text-decoration:underline;">https://www.youtube.com/user/ThinMatrix</i>)
 * .obj file reader code
 */
public class OBJFileReader {
	
	public static RawModel loadOBJFile(InputStream objFile) {
		try (Scanner reader = new Scanner(objFile)) {
			List<Vector3f> vertices = new ArrayList<Vector3f>();
			List<Vector2f> textures = new ArrayList<Vector2f>();
			List<Vector3f> normals = new ArrayList<Vector3f>();
			while (reader.hasNextLine()) {
				String[] tokens = reader.nextLine().split(" ");
				if (tokens[0].equals("v")) {
					if (tokens.length < 4) {
						continue;
					}
					Vector3f vertex = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
					vertices.add(vertex);
				} else if (tokens[0].equals("vt")) {
					if (tokens.length < 3) {
						continue;
					}
					Vector2f texture = new Vector2f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]));
					textures.add(texture);
				} else if (tokens[0].equals("vn")) {
					if (tokens.length < 4) {
						continue;
					}
					Vector3f normal = new Vector3f(Float.parseFloat(tokens[1]), Float.parseFloat(tokens[2]), Float.parseFloat(tokens[3]));
					normals.add(normal);
				} else if (tokens[0].equals("s")) {
					break;
				}
			}
			float[] texsArray = new float[vertices.size() * 2];
			float[] normsArray = new float[vertices.size() * 3];
			List<Integer> indices = new ArrayList<Integer>();
			while (reader.hasNextLine()) {
				String[] tokens = reader.nextLine().split(" ");
				if (!tokens[0].equals("f") || tokens.length < 4) continue;
				String[] verts1 = tokens[1].split("/");
				String[] verts2 = tokens[2].split("/");
				String[] verts3 = tokens[3].split("/");
				processVertex(verts1, indices, textures, normals, texsArray, normsArray);
				processVertex(verts2, indices, textures, normals, texsArray, normsArray);
				processVertex(verts3, indices, textures, normals, texsArray, normsArray);
			}
			float[] vertsArray = new float[vertices.size() * 3];
			int pointer = 0;
			for (Vector3f vertex : vertices) {
				vertsArray[pointer++] = vertex.x;
				vertsArray[pointer++] = vertex.y;
				vertsArray[pointer++] = vertex.z;
			}
			int[] indicesArray = new int[indices.size()];
			for (int i = 0; i < indices.size(); i++) indicesArray[i] = indices.get(i);
			return GLDataManager.loadToVAO(vertsArray, texsArray, normsArray, indicesArray);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void processVertex(String[] vert, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] texsArray, float[] normsArray) {
		int vertPointer = Integer.parseInt(vert[0]) - 1;
		indices.add(vertPointer);
		Vector2f tex = textures.get(Integer.parseInt(vert[1]) - 1);
		texsArray[vertPointer * 2] = tex.x;
		texsArray[vertPointer * 2 + 1] = tex.y;
		Vector3f norm = normals.get(Integer.parseInt(vert[2]) - 1);
		normsArray[vertPointer * 3] = norm.x;
		normsArray[vertPointer * 3 + 1] = norm.y;
		normsArray[vertPointer * 3 + 2] = norm.z;
	}
}
