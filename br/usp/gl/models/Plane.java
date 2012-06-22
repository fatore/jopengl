package br.usp.gl.models;

public class Plane extends Model {
	
	private static final float[] positions_ = new float[] {
		
		-0.5f, -0.5f, 0.0f, 
		 0.5f, -0.5f, 0.0f, 
		 0.5f, 0.5f, 0.0f, 
		 -0.5f, 0.5f, 0.0f, 
	};
	
	private static final int[] indices_ = new int[] {
		
		0,1,2,	0,2,3
	};
	
	private static final float[] textureCoords_ = new float[] {
		
		0.0f, 0.0f,
		1.0f, 0.0f,
		1.0f, 1.0f,
		0.0f, 1.0f,
		
	};
	
	@Override
	public void load() {
		
		this.positions = positions_;
		this.indices = indices_;
		this.textureCoord = textureCoords_;
	}
}























