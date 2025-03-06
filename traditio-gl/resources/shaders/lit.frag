in vec3 vertex_pos;
in vec4 vertex_color;
in vec2 vertex_texCoord;
in vec3 worldNormal;

#define MAX_LIGHTS 8

#define LIGHT_DIRECTIONAL 0
#define LIGHT_POINT 1
#define LIGHT_SPOT 2

struct Light {
	
	int enabled;
	int padding1;    // 4 bytes padding (std140 requires next member to align to 8 or 16)
	int padding2;    // 4 bytes padding
	int padding3;    // 4 bytes padding
	
	// Point and Spot, Alpha 0 -> Directional, 1 -> Point.
	vec4 position;
	
	// RGBA Color
	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
    
	// Spot
	vec3 direction;
	
	float spotExponent;
	
	vec3 attenuation;
	
	float spotCutoff;
};

layout(std140, binding = 1) uniform LightBlock {
    
	Light lights[MAX_LIGHTS]; 
};


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

uniform mat4 modelView;

out vec4 fragColor;

void main() {

	// Normalize normal
	vec3 normal = normalize(worldNormal);
	vec4 eyePos = modelView * vec4(vertex_pos, 1.0);
	
	vec3 litAmbient = vec3(0.0);
	vec3 litDiffuse = vec3(0.0);
	
	Light light;
	for (int i = 0; i < MAX_LIGHTS; i++)
	{
		light = lights[i];
		if (light.enabled == 0) continue;
		
		vec3 lightPos = (modelView * vec4(lights[i].position.xyz, 1.0)).xyz;
		
		vec3 lightDir = normalize(lightPos - eyePos.xyz);
		float NdotL = max(dot(normal, lightDir), 0.0);
		vec3 diffuse = NdotL * light.diffuse.rgb;
		
		litAmbient += light.ambient.rgb;
		litDiffuse += diffuse;
	}

	#ifdef USE_TEXTURE
      vec4 diffuseColor = texture2D(texture_sampler, vertex_texCoord);
    #else
      vec4 diffuseColor = vec4(1.0);
    #endif
	
	vec4 color = clamp(diffuseColor * vertex_color, 0.0, 1.0);
	color = clamp(color * vec4(litAmbient + litDiffuse, 1.0), 0.0, 1.0);
	
	fragColor = color;
	
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
		fragColor = mix(fogColor, color, fogFactor);
		fragColor.a = color.a;
	#endif
}
