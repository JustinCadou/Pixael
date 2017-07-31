#version 400 core

in vec3 vertex;
in vec2 texCoords;
in vec3 normal;

out vec2 pass_texCoords;
out vec3 toCamVec;
out vec3 normSurf;

uniform mat4 transMat;
uniform mat4 projMat;
uniform mat4 viewMat;
uniform vec3 sunPos;
uniform vec3 moonPos;

void main(void) {
	vec4 vertWorldPos = transMat * vec4(vertex, 1.0);
	gl_Position = projMat * viewMat * vertWorldPos;
	pass_texCoords = texCoords;
	toCamVec = (inverse(viewMat) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - vertWorldPos.xyz;
	normSurf = (transMat * vec4(normal, 1.0)).xyz;
}