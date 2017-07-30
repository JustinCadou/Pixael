#version 400 core

in vec3 vertex;

out vec3 vert;

uniform mat4 transMat;
uniform mat4 projMat;
uniform mat4 rotMat;

void main(void) {
	gl_Position = projMat * rotMat * transMat * vec4(vertex, 1.0);
	vert = vertex;
}