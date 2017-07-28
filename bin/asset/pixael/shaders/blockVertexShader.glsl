#version 400 core

in vec3 vertex;
in vec2 texCoords;
in vec3 normal;

out vec2 pass_texCoords;

uniform mat4 transMat;
uniform mat4 projMat;
uniform mat4 viewMat;

void main(void) {
	vec4 vertWorldPos = transMat * vec4(vertex, 1.0);
	gl_Position = projMat * viewMat * vertWorldPos;
	pass_texCoords = texCoords;
}