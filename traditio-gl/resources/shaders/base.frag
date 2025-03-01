in vec4 vertex_color;
in vec2 vertex_texCoord;

#ifdef USE_TEXTURE
  uniform sampler2D texture_sampler;
#endif

out vec4 fragColor;

void main() {

	#ifdef USE_TEXTURE
      vec4 diffuseColor = texture2D(texture_sampler, vertex_texCoord);
    #else
      vec4 diffuseColor = vec4(1.0);
    #endif

	fragColor = diffuseColor * vertex_color;
}
