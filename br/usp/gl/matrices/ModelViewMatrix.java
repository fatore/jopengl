package br.usp.gl.matrices;

import javax.media.opengl.GL4;

public class ModelViewMatrix extends Matrix4 {
	
	private GL4 gl;
	
	private int handle;

	public void init(final GL4 gl, int handle) {

		this.gl = gl;
		this.handle = handle;
	}
	
	public void bind() {

		gl.glUniformMatrix4fv(handle, 1, false, this.matrix, 0);
	}
}