package br.usp.gl.core;

import javax.media.opengl.GL4;

import br.usp.gl.buffers.ArrayBuffer;
import br.usp.gl.buffers.ArrayElementsBuffer;

public abstract class Model {
	
	protected GL4 gl;
	
	protected float[] positions;
	protected float[] normals;
	protected int[] indices; 
	
	private ArrayBuffer positionsBuffer;
	private ArrayBuffer normalsBuffer;
	
	private ArrayElementsBuffer indicesBuffer;
	
	private int positionHandle;
	private int normalsHandle;
	
	int[] vaoHandle;
	
	abstract public void load();
	
	public void init(GL4 gl, int positionHandle) {
		
		init(gl, positionHandle, -1);
	}
	
	public void init(GL4 gl, int positionHandle, int normalsHandle) {
		
		load();
		
		this.gl = gl;
		this.positionHandle = positionHandle;
		this.normalsHandle = normalsHandle;
		
		initBuffers();
	}
	
	private void initBuffers() {
		
		if (positions != null) {
			positionsBuffer = new ArrayBuffer(gl, positions, 3, positionHandle);
		}
		
		if (normals != null && normalsHandle >= 0) {
			normalsBuffer = new ArrayBuffer(gl, normals, 3, normalsHandle);
		}
		
		if (indices != null) {
			indicesBuffer = new ArrayElementsBuffer(gl, indices);
		}
		
		vaoHandle = new int[1];
		gl.glGenVertexArrays(1, vaoHandle, 0);
		gl.glBindVertexArray(vaoHandle[0]);
	}
	
	public void bind() {

		if (positionsBuffer != null) {
			positionsBuffer.bind();
		}
		
		if (normalsBuffer != null) {
			normalsBuffer.bind();
		}
		
		if (indicesBuffer != null) {
			indicesBuffer.bind();
		}
	}
	
	public void draw(int primitive) {
		
		gl.glBindVertexArray(vaoHandle[0]);
		
		if (indicesBuffer != null) {
			gl.glDrawElements(primitive, indices.length, GL4.GL_UNSIGNED_INT, 0);
		} else {
			gl.glDrawArrays(primitive, 0, positions.length / 3);
		}
	}
	
	public void dispose() {
		
		if (positionsBuffer != null) {
			positionsBuffer.dispose();
		}
		
		if (normalsBuffer != null) {
			normalsBuffer.dispose();
		}
		
		if (indicesBuffer != null) {
			indicesBuffer.dispose();
		}
	}
}
