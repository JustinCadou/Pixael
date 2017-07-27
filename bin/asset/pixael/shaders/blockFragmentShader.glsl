#version 400 core

in vec2 pass_texCoords;

out vec4 pxColor;

uniform sampler2D sampler;

void main(void) {
	pxColor = texture(sampler, pass_texCoords);
}