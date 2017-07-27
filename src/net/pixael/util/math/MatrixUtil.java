package net.pixael.util.math;

import net.fantasticfantasy.mainkit.maths.Matrix4f;
import net.fantasticfantasy.mainkit.maths.Vector3f;
import net.pixael.Pixael;
import net.pixael.world.Camera;

/**
 * Based on ThinMatrix's (YouTube Channel 
 * <i style="color:blue;text-decoration:underline;">https://www.youtube.com/user/ThinMatrix</i>)
 * .obj file reader code
 */
public class MatrixUtil {
	
	public static Matrix4f createTransformationMatrix(Transformation transformation) {
		Matrix4f matrix = new Matrix4f();
		Matrix4f.setIdentity(matrix);
		Matrix4f.translate(transformation.getTranslation(), matrix, matrix);
		Vector3f rot = transformation.getRotation();
		Matrix4f.rotate(rot.x, new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate(rot.y, new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate(rot.z, new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(transformation.getScale(), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f matrix = new Matrix4f();
		Matrix4f.setIdentity(matrix);
		Vector3f rotation = camera.getRotation();
		Matrix4f.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotation.z), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.translate(camera.getPosition(), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createProjectionMatrix(float fov, float nearPlane, float farPlane) {
		int[] winDim = Pixael.getPixael().getWindowSize();
		float aspectRatio = (float) winDim[0] / (float) winDim[1];
		float yscale = (float) (1f / Math.tan(Math.toRadians(fov / 2f))) * aspectRatio;
		float xscale = yscale / aspectRatio;
		float frustumLength = farPlane - nearPlane;
		Matrix4f projmat = new Matrix4f();
		Matrix4f.setIdentity(projmat);
		projmat.m00 = xscale;
		projmat.m11 = yscale;
		projmat.m22 = -((farPlane + nearPlane) / frustumLength);
		projmat.m23 = -1f;
		projmat.m32 = -((2 * farPlane * nearPlane) / frustumLength);
		projmat.m33 = 0f;
		return projmat;
	}
}
