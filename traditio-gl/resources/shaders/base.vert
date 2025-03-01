#version 330 core

layout (location = 0) in vec3 in_position;
layout (location = 1) in vec4 in_color;
layout (location = 2) in vec2 in_texCoord;
layout (location = 3) in vec3 in_normal;

uniform mat4 projection;
uniform mat4 modelView;

out vec4 vertex_color;
out vec2 vertex_texCoord;

void main() {
	vertex_color = in_color;
	vertex_texCoord = in_texCoord;

    gl_Position = projection * modelView * vec4(in_position, 1.0);
}