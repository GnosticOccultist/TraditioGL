in vec4 vertex_color;
in vec2 vertex_texCoord;

#ifdef USE_TEXTURE
	uniform sampler2D texture_sampler;
#endif

#ifdef USE_FOG
	uniform vec4 fogColor;
	in float fog_distance;
	
	#ifdef FOG_LINEAR
		uniform vec2 fogLinearRange;
	#endif
	#if (defined(FOG_EXP) || defined(FOG_EXP2))
		uniform float fogDensity;
	#endif
#endif

out vec4 fragColor;

void main() {

	#ifdef USE_TEXTURE
      vec4 diffuseColor = texture2D(texture_sampler, vertex_texCoord);
    #else
      vec4 diffuseColor = vec4(1.0);
    #endif
	
	fragColor = diffuseColor * vertex_color;
	
	#ifdef USE_FOG
		float fogFactor = 1.0;
		#ifdef FOG_LINEAR
			fogFactor = (fogLinearRange.y - fog_distance) / (fogLinearRange.y - fogLinearRange.x);
		#endif
		#ifdef FOG_EXP
			fogFactor = 1.0 / exp(fog_distance * fogDensity);
		#endif
		#ifdef FOG_EXP2
			fogFactor = 1.0 / exp((fog_distance * fogDensity) * (fog_distance * fogDensity));
		#endif
		
		fogFactor = clamp(fogFactor, 0.0, 1.0);
		fragColor = mix(fogColor, diffuseColor, fogFactor);
		fragColor.a = diffuseColor.a;
	#endif
}
