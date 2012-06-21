package br.usp.gl.matrices;

public class Matrix3 extends Matrix {

	public Matrix3() {
		
		this.matrix = new float[9];
	}
	
	public void loadIdentity() {
		
        this.matrix[0] = 1;
        this.matrix[1] = 0;
        this.matrix[2] = 0;
        this.matrix[3] = 0;
        this.matrix[4] = 1;
        this.matrix[5] = 0;
        this.matrix[6] = 0;
        this.matrix[7] = 0;
        this.matrix[8] = 1;
	}
	
	public void transpose() {
		
        float a01 = this.matrix[1], a02 = this.matrix[2],
            a12 = this.matrix[5];

        this.matrix[1] = this.matrix[3];
        this.matrix[2] = this.matrix[6];
        this.matrix[3] = a01;
        this.matrix[5] = this.matrix[7];
        this.matrix[6] = a02;
        this.matrix[7] = a12;
	}
	
	public void print() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.printf("%.5f ", this.matrix[i + j * 3]);
			}
			System.out.println();
		}
		System.out.println();
	}
}
