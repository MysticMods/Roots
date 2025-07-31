#version 150

uniform sampler2D NoiseTexture;
uniform float DissolveThreshold;
uniform float NoiseScale;

in vec3 modelPos;

void main() {
    vec2 uv = modelPos.xy * NoiseScale;
    float alpha = texture(NoiseTexture, uv).a;
    if (alpha < DissolveThreshold) discard;
}
