#version 120
varying vec3 normal;
varying float shift;
varying vec3 position;
varying vec4 uv;
uniform int time;
uniform sampler2D sampler;
varying vec4 color;
uniform sampler2D lightmap;
uniform mat4 modelview;

float rand2(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

vec3 rand3(vec3 co){
    return vec3(rand2(co.xz)-0.5f,rand2(co.yx)-0.5f,rand2(co.zy)-0.5f);
}

void main()
{
    vec4 pos = gl_ModelViewProjectionMatrix * gl_Vertex;
	mat3 normalMatrix = mat3(gl_ModelViewProjectionMatrix);
	normal = gl_Normal * mat3(modelview);
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	
	position = gl_Vertex.xyz;
	
	gl_TexCoord[0] = gl_MultiTexCoord0;
	gl_TexCoord[1] = gl_MultiTexCoord1;
	
	gl_FrontColor = gl_Color;
}