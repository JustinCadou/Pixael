#version 400 core

in vec2 pass_texCoords;
in vec3 toCamVec;
in vec3 normSurf;

out vec4 pxColor;

uniform sampler2D sampler;
uniform vec3 sunColor;
uniform vec3 moonColor;

void main(void) {
	vec3 unitNorm = normalize(normSurf);
	vec3 unitCam = normalize(toCamVec);
	float shade = max(dot(unitNorm, unitCam), 0.9);
	pxColor = texture(sampler, pass_texCoords) * shade;
}