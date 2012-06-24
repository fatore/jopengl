package br.usp.gl.matrices;

import javax.media.opengl.GL3;

public class NormalMatrix extends Matrix3 {

	private GL3 gl;
	private int handle;

	public void init(final GL3 gl, int handle) {

		this.gl = gl;
		this.handle = handle;
	}
	
	public void update(Matrix4 mvMatrix) {
		
		mvMatrix.toInverseMat3(this.matrix);
        this.transpose();
	}
	
	public void extractMatrix(Matrix mvMatrix) {
		
		float[] source = mvMatrix.getMatrix();
		
		matrix[0] = source[0];
	    matrix[1] = source[1];
	    matrix[2] = source[2];

	    matrix[3] = source[4];
	    matrix[4] = source[5];
	    matrix[5] = source[6];

	    matrix[6] = source[8];
	    matrix[7] = source[9];
	    matrix[8] = source[10];
	}
	
	public void bind() {

		gl.glUniformMatrix3fv(handle, 1, false, this.matrix, 0);
	}
}
