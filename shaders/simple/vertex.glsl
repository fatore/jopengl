#version 420 core

uniform mat4 uPMatrix;
uniform mat4 uMVMatrix;
uniform mat3 uNMatrix;

uniform vec3 uLightDirection;

in vec3 aVertexPosition;
in vec3 aVertexNormal;

out vec3 vNormal;
out vec3 vEye;

uniform bool uPancake;

void main()
{
    vec4 vertex = uMVMatrix * vec4(aVertexPosition, 1);

	vEye = -vec3(vertex);
	
	vNormal = uNMatrix * aVertexNormal;
    
    if (!uPancake) {
    	gl_Position = uPMatrix * uMVMatrix * vec4(aVertexPosition, 1);
    } else {
	    gl_Position = uPMatrix * uMVMatrix * vec4(aVertexPosition.x, 0.0, aVertexPosition.z, 1.0);
    }
}