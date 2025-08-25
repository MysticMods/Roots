#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;
uniform sampler2D NoiseTexture;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform float DissolveThreshold;

in float vertexDistance;
in vec4 vertexColor;
in vec4 lightMapColor;
in vec4 overlayColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec2 atlasSize = vec2(textureSize(Sampler0, 0));
    vec2 atlasCoord = texCoord0 * atlasSize;
    vec4 noise = texture(NoiseTexture, fract(atlasCoord / 16.0));
    if (noise.a < DissolveThreshold) {
        discard;
    }

    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    color.rgb = mix(overlayColor.rgb, color.rgb, overlayColor.a);
    color *= lightMapColor;
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
