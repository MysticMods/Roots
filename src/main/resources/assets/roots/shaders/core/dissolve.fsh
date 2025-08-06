#version 150

#moj_import <fog.glsl>

uniform sampler2D NoiseTexture;
uniform float DissolveThreshold;

in vec2 texCoord0;

void main() {
    vec4 color = texture(NoiseTexture, texCoord0);
    if (color.a < DissolveThreshold) {
        discard;
    }
}
