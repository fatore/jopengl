#version 150

struct LightProperties
{
	vec3 direction;
	vec4 ambientColor;
	vec4 diffuseColor;
	vec4 specularColor;
};

struct MaterialProperties
{
	vec4 ambientColor;
	vec4 diffuseColor;
	vec4 specularColor;
	float specularExponent;
};

uniform LightProperties u_light;
uniform MaterialProperties u_material;

in vec3 v_normal;
in vec3 v_eye;

out vec4 fragColor;

in vec3 N;

void main(void) {


	vec3 L = normalize(u_light.direction);
	vec4 Idiff = u_light.diffuseColor * u_material.diffuseColor * max(dot(N,L), 0.0);
	Idiff = clamp(Idiff, 0.0, 1.0); 
	
	//fragColor = Idiff;

	// ambient coeficient
	vec4 color = u_light.ambientColor * u_material.ambientColor;
	
	vec3 normal = normalize(v_normal);
	
	float nDotL = max(dot(u_light.direction, normal), 0.0);
	
	if (nDotL > 0.0) {
	
		// diffuse coeficient
		//color += u_light.diffuseColor * u_material.diffuseColor * nDotL;
	
		vec3 eye = normalize(v_eye);
		
		// Incident vector is opposite light direction vector.
		vec3 reflection = reflect(-u_light.direction, normal);
		
		float eDotR = max(dot(eye, reflection), 0.0);
		
		// specular coeficient		
		//color += u_light.specularColor * u_material.specularColor * (eDotR, u_material.specularExponent);
	}
 	
	fragColor = color;
}
