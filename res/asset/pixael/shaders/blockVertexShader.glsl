#version 400 core

in vec3 vertex;
in vec2 texCoords;
in vec3 normal;

out vec2 pass_texCoords;
out vec3 surfaceNorm;
out vec3 toLightVec;
out vec3 toCameraVec;

uniform mat4 transMat;
uniform mat4 projMat;
uniform mat4 viewMat;
uniform vec3 lightPos;

void main(void) {
	vec4 vertWorldPos = transMat * vec4(vertex, 1.0);
	gl_Position = projMat * viewMat * vertWorldPos;
	pass_texCoords = texCoords;
	surfaceNorm = (transMat * vec4(normal, 0.0)).xyz;
	toLightVec = lightPos - vertWorldPos.xyz;
	toCameraVec = (inverse(viewMat) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - vertWorldPos.xyz;
}