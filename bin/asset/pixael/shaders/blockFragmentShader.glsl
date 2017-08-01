#version 400 core

in vec2 pass_texCoords;
in vec3 surfaceNorm;
in vec3 toLightVec;
in vec3 toCameraVec;

out vec4 pxColor;

uniform sampler2D sampler;
uniform vec3 lightColor;

void main(void) {
	vec3 unitNormal = normalize(surfaceNorm);
	vec3 unitLightVec = normalize(toLightVec);
	float normDotLight = dot(unitNormal, unitLightVec);
	float brightness = max(normDotLight, 0.0);
	vec3 ambientOcclusion = vec3(0.3, 0.3, 0.3) * (1.05 - brightness);
	vec3 diffuse = brightness * lightColor + ambientOcclusion;
	pxColor = texture(sampler, pass_texCoords) * vec4(diffuse, 1.0);
}