#version 330

layout(location = 0) in vec3 vertexPosition;

uniform mat4 projMat;

void main() {

    gl_Position = projMat * vec4(vertexPosition, 1.0);

}
