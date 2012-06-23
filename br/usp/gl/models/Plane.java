package br.usp.gl.models;

public class Plane extends Model {
	
	private static final float[] positions_ = new float[] {
		
		   -1.0f, -1.0f, 0.0f,
		   +1.0f, -1.0f, 0.0f,
		   -1.0f, +1.0f, 0.0f,
		   +1.0f, +1.0f, 0.0f,
	};
	
	private static final float[] normals_ = new float[] {
		
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f,
		0.0f, 0.0f, 1.0f
	};
	
	@SuppressWarnings("unused")
	private static final float[] tangents_ = new float[] {
		
		1.0f, 0.0f, 0.0f,
		1.0f, 0.0f, 0.0f,
		1.0f, 0.0f, 0.0f,
		1.0f, 0.0f, 0.0f
	};
	
	private static final float[] textureCoords_ = new float[] {
		
		0.0f, 0.0f, 
		1.0f, 0.0f, 
		0.0f, 1.0f, 
		1.0f, 1.0f
	};
	
	private static final int[] indices_ = new int[] {
		
		0, 1, 2, 1, 3, 2
	};
	
	
	public Plane() {
		
		this(1);
	}
	
	public Plane(float halfExtend) {
		
		this(halfExtend, halfExtend);
	}
	
	public Plane(float horizontalExtend, float verticalExtend) {
		
		this.positions = positions_;
		this.normals = normals_;
		this.texCoords = textureCoords_;
		this.indices = indices_;
		
		for (int i = 0; i < positions.length / 3; i++) {
			positions[i * 3 + 0] *= horizontalExtend;
			positions[i * 3 + 1] *= verticalExtend;
		}
	}
}























