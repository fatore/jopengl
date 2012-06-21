package br.usp.gl.matrices;

import javax.media.opengl.GL4;

public class NormalMatrix extends Matrix3 {

	private GL4 gl;
	private int handle;

	public void init(final GL4 gl, int handle) {

		this.gl = gl;
		this.handle = handle;
	}
	
	public void update(Matrix4 mvMatrix) {
		
		mvMatrix.toInverseMat3(this.matrix);
        this.transpose();
	}
	
	public void bind() {

		gl.glUniformMatrix3fv(handle, 1, false, this.matrix, 0);
	}
}
