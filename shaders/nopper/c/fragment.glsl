#version 150

uniform vec3 u_lightDirection;
uniform vec4 u_color;

in vec3 v_normal;

out vec4 fragColor;

void main(void)
{
	// Lambert without emissive color. al is the ambient, hard coded light factor.
	// <ambient> * al + <diffuse> * max(N*L, 0)
 	vec4 v_color = u_color * 0.3 + u_color * max(dot(v_normal, u_lightDirection), 0.0);
 	
	fragColor = v_color;
}
