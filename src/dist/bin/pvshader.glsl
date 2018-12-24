#version 330
layout(location = 0) in vec3 vertexPosition;

out vec3 colour;

void main() {

    colour = vec3(1, 1, 1);
    gl_Position = vec4(vertexPosition, 1.0);

}
