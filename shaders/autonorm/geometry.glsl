#version 420 core

uniform mat4 u_modelViewMatrix;

uniform mat3 u_normalMatrix;

layout(triangles) in;

layout(triangle_strip, max_vertices = 3) out;

in vec3 vPosition[3];

out vec3 gFacetNormal;
out vec3 gTriDistance;

void main()
{
    vec3 A = vPosition[2] - vPosition[0];
    vec3 B = vPosition[1] - vPosition[0];
    gFacetNormal = u_normalMatrix * normalize(cross(A, B));
    
    gTriDistance = vec3(1, 0, 0);
    gl_Position = gl_in[0].gl_Position; EmitVertex();

    gTriDistance = vec3(0, 1, 0);
    gl_Position = gl_in[1].gl_Position; EmitVertex();

    gTriDistance = vec3(0, 0, 1);
    gl_Position = gl_in[2].gl_Position; EmitVertex();

    EndPrimitive();
}