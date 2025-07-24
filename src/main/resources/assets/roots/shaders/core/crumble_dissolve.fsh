#version 150

#moj_import <fog.glsl>

uniform sampler2D CrumbleSampler;

uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

in float vertexDistance;
in vec2 texCoord0;       // UV for CrumbleSampler
in vec4 vertexColor;     // Only .a used

out vec4 fragColor;

void main() {
    vec4 crumbleColor = texture(CrumbleSampler, texCoord0);

    // Only keep pixels that exist in the Crumble texture
    //if (crumbleColor.a == 0.0) discard;

    // Dissolve effect: discard pixels below dissolve threshold
    //if (crumbleColor.a < vertexColor.a) discard;

    vec4 finalColor = crumbleColor * ColorModulator;

    //fragColor = linear_fog(finalColor, vertexDistance, FogStart, FogEnd, FogColor);
    fragColor = vec4(1, 0, 0, 1);
}
