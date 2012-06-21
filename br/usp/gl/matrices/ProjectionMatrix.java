package br.usp.gl.matrices;

import javax.media.opengl.GL4;

public class ProjectionMatrix extends Matrix4 {
	
	private GL4 gl;
	private int handle;

	public void init(final GL4 gl, int handle) {

		this.gl = gl;
		this.handle = handle;
	}

	public void bind() {

		gl.glUniformMatrix4fv(handle, 1, false, this.matrix, 0);
	}

	public void perspective(int x, int y, int width, int height) {

		float fovy = 45f;
		float near = 0.1f;
		float far = 100.0f;

		float f = (float) (1.0 / Math.tan(Math.toRadians(fovy / 2.0)));
		
		this.matrix[0] = f / ((float) width / (float) height);
		this.matrix[5] = f;
		this.matrix[10] = (far + near) / (near - far);
		this.matrix[14] = (2 * far * near) / (near - far);
		this.matrix[11] = -1;
	}
	
	public void ortho(float left, float right, float bottom, float top, float near, float far) {
		
		float 
		rl = (right - left),
		tb = (top - bottom),
        fn = (far - near);
		
		this.matrix[0] = 2 / rl;
		this.matrix[1] = 0;
		this.matrix[2] = 0;
		this.matrix[3] = 0;
		this.matrix[4] = 0;
		this.matrix[5] = 2 / tb;
		this.matrix[6] = 0;
		this.matrix[7] = 0;
		this.matrix[8] = 0;
		this.matrix[9] = 0;
		this.matrix[10] = -2 / fn;
		this.matrix[11] = 0;
		this.matrix[12] = -(left + right) / rl;
		this.matrix[13] = -(top + bottom) / tb;
		this.matrix[14] = -(far + near) / fn;
		this.matrix[15] = 1;
	}
	
}