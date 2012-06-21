package br.usp.gl.util;

import java.io.BufferedReader;
import java.io.FileReader;

public class JsonObjectReader {
	
	private float[] positions;
	private float[] normals;
	private int[] indices;
	
	public float[] getPositions() {return positions;}
	public float[] getNormals() {return normals;}
	public int[] getIndices() {return indices;}
	
	public JsonObjectReader(String filename) throws Exception {
		
		BufferedReader in = new BufferedReader(new FileReader(filename));

		in.readLine();

		String line = in.readLine();

		while (line != null) {

			String[] split = line.split(":");

			switch (split[0].trim()) {

			case "\"vertexPositions\"":
				readPositions(split[1].trim());
				break;

			case "\"indices\"":
				readIndices(split[1].trim());
				break;
			
			case "\"vertexNormals\"":
				readNormals(split[1].trim());
				break;
			}
			
			line = in.readLine();
		}
		
	}
	
	private void readPositions(String positionsLine) {
		
		String[] split = positionsLine.split(",");
		split[0] = split[0].replaceAll("\\[", "");
		split[split.length-1] = split[split.length - 1].replaceAll("]", "");
		
		positions = new float[split.length];
		
		int i = 0;
		try {
			for (i = 0; i < positions.length; i++) {
				positions[i] = Float.parseFloat(split[i]);			
			}
		} catch (Exception e) {
			System.err.println(split[i]);
			e.printStackTrace();
		}
	}
	
	private void readNormals(String normalsLine) {
		
		String[] split = normalsLine.split(",");
		split[0] = split[0].replaceAll("\\[", "");
		split[split.length-1] = split[split.length - 1].replaceAll("]", "");
		
		normals = new float[split.length];
		
		int i = 0;
		try {
			for (i = 0; i < positions.length; i++) {
				normals[i] = Float.parseFloat(split[i]);			
			}
		} catch (Exception e) {
			System.err.println(split[i]);
			e.printStackTrace();
		}
	}
	
	private void readIndices(String indicesLine) {
		
		String[] split = indicesLine.split(",");
		split[0] = split[0].replaceAll("\\[", "");
		split[split.length-1] = split[split.length - 1].replaceAll("]", "");
		
		indices = new int[split.length];
		
		int i = 0;
		try {
			for (i = 0; i < indices.length; i++) {
				indices[i] = Integer.parseInt(split[i]);			
			}
		} catch (Exception e) {
			System.err.println(split[i]);
			e.printStackTrace();
		}
	}
}
