layout (location = 0) in vec3 in_position;
layout (location = 1) in vec4 in_color;
layout (location = 2) in vec2 in_texCoord;
layout (location = 3) in vec3 in_normal;
layout (location = 4) in float in_fogCoord;

uniform mat4 projection;
uniform mat4 modelView;

#ifdef USE_FOG
	out float fog_distance;
#endif

out vec3 vertex_pos;
out vec4 vertex_color;
out vec2 vertex_texCoord;
out vec3 worldNormal;

void main() {
	vertex_pos = in_position;
	vertex_color = in_color;
	vertex_texCoord = in_texCoord;
	
	worldNormal = normalize(mat3(modelView) * in_normal);
	
	vec4 viewPosition = modelView * vec4(in_position, 1.0);
	gl_Position = projection * viewPosition;
	
	#ifdef USE_FOG
		#ifdef USE_FOG_COORD
			// Fog distance defined as per vertex fog coord.
			// 0 for non-fogged vertex.
			fog_distance = in_fogCoord;
		#else
			// Fog distance defined as fragment depth.
			fog_distance = abs(viewPosition.z);
		#endif
		
	#endif
}