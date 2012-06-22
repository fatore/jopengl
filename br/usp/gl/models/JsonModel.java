package br.usp.gl.models;

import br.usp.gl.util.JsonObjectReader;

public class JsonModel extends Model {

	protected String filename;
	
	
	public JsonModel(String filename) {

		this.filename = filename;
	}

	@Override
	public void load() {
		
		try {
			JsonObjectReader reader = new JsonObjectReader(filename);
			
			this.positions = reader.getPositions();
			this.normals = reader.getNormals();
			this.indices = reader.getIndices();
			
		} catch (Exception e) {
			System.err.println("Failed to load json object.");
			e.printStackTrace();
		}
	}
}
