#version 150

in vec3 Position;
in vec4 Color;
in vec2 UV0;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

out vec2 texCoord0;
out vec4 vertexColor;
out float vertexDistance;

void main() {
    vec4 worldPosition = ModelViewMat * vec4(Position, 1.0);
    gl_Position = ProjMat * worldPosition;
    vertexDistance = length(worldPosition.xyz);
    texCoord0 = UV0;
    vertexColor = Color;
}
