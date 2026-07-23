#version 150

#moj_import <fog.glsl>

in vec3 Position;
in vec2 UV0;
in vec4 Color;
in ivec2 UV2;
in ivec2 UV1;

uniform sampler2D Sampler2;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;
uniform int FogShape;

out float vertexDistance;
out vec2 texCoord0;
out vec4 vertexColor;
out float vAgeFrac;

void main() {
    vec3 pos = Position;

    vec3 cameraRight = vec3(ModelViewMat[0][0], ModelViewMat[1][0], ModelViewMat[2][0]);
    vec3 cameraUp = vec3(ModelViewMat[0][1], ModelViewMat[1][1], ModelViewMat[2][1]);

    vAgeFrac = float(UV1.x) / float(max(UV1.y, 1));

    float side = (UV0.x - 0.5) * 2.0;
    float heightWeight = UV0.y;

    const float riseAmount = 4;
    const float pinchAmount = 6;

    pos += cameraUp * heightWeight * vAgeFrac * riseAmount;

    pos -= cameraRight * side * heightWeight * vAgeFrac * pinchAmount;

    gl_Position = ProjMat * ModelViewMat * vec4(pos, 1.0);

    vertexDistance = fog_distance(pos, FogShape);
    texCoord0 = UV0;
    vertexColor = Color * texelFetch(Sampler2, UV2 / 16, 0);
}
