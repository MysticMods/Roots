#version 120
varying vec3 normal;
varying vec4 color;
varying float shift;
varying vec3 position;
varying vec4 uv;
uniform int time;
uniform int chunkX;
uniform int chunkZ;
uniform sampler2D sampler;
uniform sampler2D lightmap;
uniform vec3 playerPos;

void main()
{
	float blue = 1.0f;
	if (abs(position.x) < 8.0f && abs(position.z) < 8.0f){
		blue = 0.0f;
	}
	vec4 baseColor = gl_Color * vec4(texture2D(sampler,gl_TexCoord[0].st).xyz * (2.0f+dot(normal,gl_LightSource[0].position.xyz))/3.0f,texture2D(sampler,gl_TexCoord[0].st).w);
	vec4 light = vec4(0,0,0,1);
	float intens = max(0,1.0f-distance(playerPos,position)/8.0f) * 1.25f;
	light.x = 1.0f;
	light.y = 1.0f;
	light.z = 1.0f;
	vec4 color = (baseColor * (light * 1.25f));
	color.x = min(1.0f,color.x);
	color.y = min(1.0f,color.y);
	color.z = min(1.0f,color.z);
	color.w = min(1.0f,color.w);
	color = (1.0f-intens)*baseColor + intens*color;
	gl_FragColor = vec4(color.xyz * (intens + (1.0f-intens)*texture2D(lightmap,gl_TexCoord[1].st*vec2(1/256.0f,1/256.0f)).xyz),color.w);
}