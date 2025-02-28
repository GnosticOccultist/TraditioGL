#version 330 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec4 in_color;
layout (location = 2) in vec3 in_normal;

uniform mat4 projection;
uniform mat4 modelView;

out vec4 vertex_color;

void main() {
	vertex_color = in_color;

    gl_Position = projection * modelView * vec4(in_position, 1.0);
}