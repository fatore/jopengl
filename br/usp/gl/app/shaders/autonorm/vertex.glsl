#version 420 core

in vec3 aVertexPosition;
out vec3 vPosition;

uniform mat4 uPMatrix;
uniform mat4 uMVMatrix;

uniform bool uPancake;

void main()
{
    vPosition = aVertexPosition;
    
    if (!uPancake) {
    	gl_Position = uPMatrix * uMVMatrix * vec4(vPosition, 1);
    } else {
	    gl_Position = uPMatrix * uMVMatrix * vec4(vPosition.x, 0.0, vPosition.z, 1.0);
    }
}