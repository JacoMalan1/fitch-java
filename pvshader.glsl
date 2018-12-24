#version 330
layout(location = 0) in vec3 vertexPosition;

out vec4 colour;
uniform mat4 projMat;

void main() {

    colour = vec4(1.0, 1.0, 1.0, 1.0);
    gl_Position = vec4(vertexPosition, 0.0);

}
