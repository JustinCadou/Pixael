#version 400 core

out vec4 pxColor;

uniform vec4 color;

void main(void) {
	pxColor = color;
}