#version 150

in vec3 a_vertex;

void main(void)
{
	gl_Position = vec4(a_vertex, 1.0);
}
