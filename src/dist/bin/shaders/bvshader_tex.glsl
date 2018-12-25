/*

A platformer game written using OpenGL.
    Copyright (C) 2017-2018  Jaco Malan

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

*/

#version 330

layout(location = 0) in vec3 vertexPosition;
layout(location = 1) in vec2 bufferTexCoord;

out vec2 texCoord;
uniform mat4 projMat;

void main() {

    texCoord = bufferTexCoord;
    gl_Position = projMat * vec4(vertexPosition.x, vertexPosition.y, 0.0, 1.0);

}
