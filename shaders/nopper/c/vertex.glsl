#version 150

uniform mat4 uMVMatrix;
uniform mat4 uPMatrix;
uniform mat3 uNMatrix;

in vec3 a_vertex;
in vec3 a_normal;

out vec3 v_normal;

void main(void) {

	// Now the normal is in world space, as we pass the light in world space.
	v_normal = uNMatrix * a_normal;

	gl_Position = uPMatrix * uMVMatrix * vec4(a_vertex, 1.0);
}


