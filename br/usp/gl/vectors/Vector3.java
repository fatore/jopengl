package br.usp.gl.vectors;

public class Vector3 extends Vector {

	public Vector3() {
		this.vector = new float[3];
	}
	
	public Vector3(float[] array) {
		this.vector = array;
	}
	
	public void normalize() {
		
		float x = this.vector[0], y = this.vector[1], z = this.vector[2],
				len = (float) Math.sqrt(x * x + y * y + z * z);

		if (len == 0) {
			this.vector[0] = 0;
			this.vector[1] = 0;
			this.vector[2] = 0;
		} else if (len == 1) {
			this.vector[0] = x;
			this.vector[1] = y;
			this.vector[2] = z;
		}

		len = 1 / len;
		this.vector[0] = x * len;
		this.vector[1] = y * len;
		this.vector[2] = z * len;
	}
	
	public static Vector3 normalize(Vector3 input) {
		
		Vector3 dest = new Vector3();

		float x = input.vector[0], y = input.vector[1], z = input.vector[2],
				len = (float) Math.sqrt(x * x + y * y + z * z);

		if (len == 0) {
			dest.vector[0] = 0;
			dest.vector[1] = 0;
			dest.vector[2] = 0;
			return dest;
		} else if (len == 1) {
			dest.vector[0] = x;
			dest.vector[1] = y;
			dest.vector[2] = z;
			return dest;
		}

		len = 1 / len;
		dest.vector[0] = x * len;
		dest.vector[1] = y * len;
		dest.vector[2] = z * len;
		return dest;
	}
	
	public void scale(float value) {
		
        this.vector[0] *= value;
        this.vector[1] *= value;
        this.vector[2] *= value;
    };
}
