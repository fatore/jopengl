#version 150

uniform mat4 uMVMatrix;
uniform mat4 uPMatrix;
uniform mat3 uNMatrix;

in vec3 a_vertex;
in vec3 a_normal;

out vec3 v_normal;
out vec3 v_eye;

out vec3 N;

void main(void) {

	vec4 vertex = uMVMatrix * vec4(a_vertex, 1.0);

	v_eye = -vec3(vertex);
	
	v_normal = uNMatrix * a_normal;

	gl_Position = uPMatrix * vertex;
	
	N = normalize(v_normal);
}


