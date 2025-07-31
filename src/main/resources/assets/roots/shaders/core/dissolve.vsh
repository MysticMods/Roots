#version 150

#moj_import <fog.glsl>

in vec3 Position;
in vec4 Color;
in vec2 UV0;
in ivec2 UV1;
in ivec2 UV2;
in vec3 Normal;

uniform sampler2D NoiseTexture;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec3 modelPos;

void main() {
    gl_Position = ProjMat * ModelViewMat * vec4(Position, 1.0);
    modelPos = Position;
}
