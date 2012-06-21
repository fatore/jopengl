package br.usp.gl.matrices;

import java.util.Stack;


public class Matrix4 extends Matrix {
	
	protected Stack<float[]> stack;
	
	public Matrix4() {
		
		this.matrix = new float[16];
		stack = new Stack<float[]>();
		loadIdentity();
	}
	
	public void print() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				System.out.printf("%.5f ", this.matrix[i + j * 4]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	// Math
	
	public void loadIdentity() {
		
		for (int i = 0; i < this.matrix.length; i++) {
			this.matrix[i] = 0.0f;
		}
		
		this.matrix[0] = 1.0f;
		this.matrix[5] = 1.0f;
		this.matrix[10] = 1.0f;
		this.matrix[15] = 1.0f;
	}
	
	public void multiply(Matrix4 multMatrix) {

		float[] mat2 = multMatrix.matrix;

		// Cache the matrix values (makes for huge speed increases!)
		float a00 = this.matrix[ 0], a01 = this.matrix[ 1], a02 = this.matrix[ 2], a03 = this.matrix[3];
		float a10 = this.matrix[ 4], a11 = this.matrix[ 5], a12 = this.matrix[ 6], a13 = this.matrix[7];
		float a20 = this.matrix[ 8], a21 = this.matrix[ 9], a22 = this.matrix[10], a23 = this.matrix[11];
		float a30 = this.matrix[12], a31 = this.matrix[13], a32 = this.matrix[14], a33 = this.matrix[15];

		// Cache only the current line of the second matrix
		float b0  = mat2[0], b1 = mat2[1], b2 = mat2[2], b3 = mat2[3];  
		this.matrix[0] = b0*a00 + b1*a10 + b2*a20 + b3*a30;
		this.matrix[1] = b0*a01 + b1*a11 + b2*a21 + b3*a31;
		this.matrix[2] = b0*a02 + b1*a12 + b2*a22 + b3*a32;
		this.matrix[3] = b0*a03 + b1*a13 + b2*a23 + b3*a33;

		b0 = mat2[4];
		b1 = mat2[5];
		b2 = mat2[6];
		b3 = mat2[7];
		this.matrix[4] = b0*a00 + b1*a10 + b2*a20 + b3*a30;
		this.matrix[5] = b0*a01 + b1*a11 + b2*a21 + b3*a31;
		this.matrix[6] = b0*a02 + b1*a12 + b2*a22 + b3*a32;
		this.matrix[7] = b0*a03 + b1*a13 + b2*a23 + b3*a33;

		b0 = mat2[8];
		b1 = mat2[9];
		b2 = mat2[10];
		b3 = mat2[11];
		this.matrix[8] = b0*a00 + b1*a10 + b2*a20 + b3*a30;
		this.matrix[9] = b0*a01 + b1*a11 + b2*a21 + b3*a31;
		this.matrix[10] = b0*a02 + b1*a12 + b2*a22 + b3*a32;
		this.matrix[11] = b0*a03 + b1*a13 + b2*a23 + b3*a33;

		b0 = mat2[12];
		b1 = mat2[13];
		b2 = mat2[14];
		b3 = mat2[15];
		this.matrix[12] = b0*a00 + b1*a10 + b2*a20 + b3*a30;
		this.matrix[13] = b0*a01 + b1*a11 + b2*a21 + b3*a31;
		this.matrix[14] = b0*a02 + b1*a12 + b2*a22 + b3*a32;
		this.matrix[15] = b0*a03 + b1*a13 + b2*a23 + b3*a33;
	}
	
	public void translate(float[] delta) {

		this.matrix[12] = this.matrix[0] * delta[0] + this.matrix[4] * delta[1] + this.matrix[8] * delta[2] + this.matrix[12];
		this.matrix[13] = this.matrix[1] * delta[0] + this.matrix[5] * delta[1] + this.matrix[9] * delta[2] + this.matrix[13];
		this.matrix[14] = this.matrix[2] * delta[0] + this.matrix[6] * delta[1] + this.matrix[10] * delta[2] + this.matrix[14];
		this.matrix[15] = this.matrix[3] * delta[0] + this.matrix[7] * delta[1] + this.matrix[11] * delta[2] + this.matrix[15];
	}

	public void rotate(float angle, float[] axis) {

		float len = (float) Math.sqrt(axis[0] * axis[0] + axis[1] * axis[1] + axis[2] * axis[2]);

		if (len != 1) {
			len = 1 / len;
			axis[0] *= len;
			axis[1] *= len;
			axis[2] *= len;
		}

		float s = (float) Math.sin(angle);
		float c = (float) Math.cos(angle);
		float t = 1 - c;
		
		float a00, a01, a02, a03,
		a10, a11, a12, a13,
		a20, a21, a22, a23,
		b00, b01, b02,
		b10, b11, b12,
		b20, b21, b22;

		a00 = this.matrix[0]; a01 = this.matrix[1]; a02 = this.matrix[2]; a03 = this.matrix[3];
		a10 = this.matrix[4]; a11 = this.matrix[5]; a12 = this.matrix[6]; a13 = this.matrix[7];
		a20 = this.matrix[8]; a21 = this.matrix[9]; a22 = this.matrix[10]; a23 = this.matrix[11];

		// Construct the elements of the rotation matrix
		b00 = axis[0] * axis[0] * t + c; b01 = axis[1] * axis[0] * t + axis[2] * s; b02 = axis[2] * axis[0] * t - axis[1] * s;
		b10 = axis[0] * axis[1] * t - axis[2] * s; b11 = axis[1] * axis[1] * t + c; b12 = axis[2] * axis[1] * t + axis[0] * s;
		b20 = axis[0] * axis[2] * t + axis[1] * s; b21 = axis[1] * axis[2] * t - axis[0] * s; b22 = axis[2] * axis[2] * t + c;


		// Perform rotation-specific matrix multiplication
		this.matrix[0] = a00 * b00 + a10 * b01 + a20 * b02;
		this.matrix[1] = a01 * b00 + a11 * b01 + a21 * b02;
		this.matrix[2] = a02 * b00 + a12 * b01 + a22 * b02;
		this.matrix[3] = a03 * b00 + a13 * b01 + a23 * b02;

		this.matrix[4] = a00 * b10 + a10 * b11 + a20 * b12;
		this.matrix[5] = a01 * b10 + a11 * b11 + a21 * b12;
		this.matrix[6] = a02 * b10 + a12 * b11 + a22 * b12;
		this.matrix[7] = a03 * b10 + a13 * b11 + a23 * b12;

		this.matrix[8] = a00 * b20 + a10 * b21 + a20 * b22;
		this.matrix[9] = a01 * b20 + a11 * b21 + a21 * b22;
		this.matrix[10] = a02 * b20 + a12 * b21 + a22 * b22;
		this.matrix[11] = a03 * b20 + a13 * b21 + a23 * b22;
	}
	
	public void push() {
		stack.push(this.matrix.clone());
	}
	
	public void pop() {
		if (stack.size() == 0) {
			System.err.println("Matrix stack is empty.");
		} else {
			this.matrix = stack.pop();
		}
	}
	
	public void toInverseMat3(float[] dest) {
		
		float a00 = this.matrix[0], a01 = this.matrix[1], a02 = this.matrix[2],
	            a10 = this.matrix[4], a11 = this.matrix[5], a12 = this.matrix[6],
	            a20 = this.matrix[8], a21 = this.matrix[9], a22 = this.matrix[10],

	            b01 = a22 * a11 - a12 * a21,
	            b11 = -a22 * a10 + a12 * a20,
	            b21 = a21 * a10 - a11 * a20,

	            d = a00 * b01 + a01 * b11 + a02 * b21,
	            id;

	        if (d == 0) {
	        	System.err.println("Error in toInverseMat3");
        	}
	        id = 1 / d;

	        dest[0] = b01 * id;
	        dest[1] = (-a22 * a01 + a02 * a21) * id;
	        dest[2] = (a12 * a01 - a02 * a11) * id;
	        dest[3] = b11 * id;
	        dest[4] = (a22 * a00 - a02 * a20) * id;
	        dest[5] = (-a12 * a00 + a02 * a10) * id;
	        dest[6] = b21 * id;
	        dest[7] = (-a21 * a00 + a01 * a20) * id;
	        dest[8] = (a11 * a00 - a01 * a10) * id;
	}
}







