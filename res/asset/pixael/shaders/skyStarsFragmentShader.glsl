#version 400 core

in vec2 pass_texCoords;
//in vec2 vert;

out vec4 pxColor;

uniform sampler2D sampler;

void main(void) {
	pxColor = texture(sampler, pass_texCoords);
	if (pxColor.a < 0.5) {
		discard;
		//pxColor = vec4(1.0, vert.y, vert.x, 1.0);
	}
}