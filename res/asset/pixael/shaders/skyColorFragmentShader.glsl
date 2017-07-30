#version 400 core

in vec3 vert;

out vec4 pxColor;

void main(void) {
	vec3 p = vec3(0.5, 0.5, -1);
	//float c = vert.z + 0.75;
	float c = length(p - vert);
	//c = 1 - c;
	//c = c / 2;
	pxColor = vec4(c, c / 2.5 - 0.15, 0.0, 1.0);
}