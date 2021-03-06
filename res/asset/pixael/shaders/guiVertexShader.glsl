#version 400 core

in vec3 vertex;
in vec2 texCoords;

out vec2 pass_texCoords;

uniform mat4 transMat;

void main(void) {
	gl_Position = transMat * vec4(vertex, 1.0);
	pass_texCoords = texCoords;
}