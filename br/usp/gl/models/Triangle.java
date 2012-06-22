package br.usp.gl.models;




public class Triangle extends Model {
	
	private static final float[] sPositions = new float[] {
		
		-0.5f, 0.0f, 0.0f, 
		 0.5f, 0.0f, 0.0f, 
		 0.0f, 0.5f, 0.0f,
	};
	
	@Override
	public void load() {
		
		this.positions = sPositions;
	}
}























