#version 330 core

in vec4 vertex_color;
in vec2 vertex_texCoord;

out vec4 fragColor;

void main() {

	fragColor = vertex_color;
}
