package br.usp.gl.matrices;

import javax.media.opengl.GL3;

import br.usp.gl.vectors.Vector3;

public class ModelViewProjectionMatrix extends Matrix4 {
	
	private GL3 gl;
	
	private int handle;

	public void init(final GL3 gl, int handle) {

		this.gl = gl;
		this.handle = handle;
	}
	
	public void bind() {

		gl.glUniformMatrix4fv(handle, 1, false, this.matrix, 0);
	}
	
	public void lookAt(float eyeX, float eyeY, float eyeZ, 
			float centerX, float centerY, float centerZ, 
			float upX, float upY, float upZ) {
		
	    Vector3 forward = new Vector3(
	    		new float[]{centerX - eyeX, centerY - eyeY, centerZ - eyeZ});

	    forward.normalize();
	    
	    Vector3 up = new Vector3(new float[]{upX, upY, upZ});

	    Vector3 side = Vector3.cross(forward, up);
	    
	    side = Vector3.normalize(side);

	    up = Vector3.cross(side, forward);

	    this.matrix[0] = side.getVector()[0];
	    this.matrix[1] = up.getVector()[0];
	    this.matrix[2] = -forward.getVector()[0];
	    this.matrix[3] = 0.0f;
	    this.matrix[4] = side.getVector()[1];
	    this.matrix[5] = up.getVector()[1];
	    this.matrix[6] = -forward.getVector()[1];
	    this.matrix[7] = 0.0f;
	    this.matrix[8] = side.getVector()[2];
	    this.matrix[9] = up.getVector()[2];
	    this.matrix[10] = -forward.getVector()[2];
	    this.matrix[11] = 0.0f;
	    this.matrix[12] = 0.0f;
	    this.matrix[13] = 0.0f;
	    this.matrix[14] = 0.0f;
	    this.matrix[15] = 1.0f;

	    this.translate(new float[]{-eyeX, -eyeY, -eyeZ});
	}
}