package br.usp.gl.models;

import javax.media.opengl.GL4;

import br.usp.gl.buffers.ArrayBuffer;
import br.usp.gl.buffers.ArrayElementsBuffer;
import br.usp.gl.core.Texture2D;

public abstract class Model {
	
	protected GL4 gl;
	
	protected float[] positions;
	protected float[] normals;
	protected float[] textureCoord;
	protected int[] indices; 
	
	private Texture2D texture;
	
	private ArrayBuffer positionsBuffer;
	private ArrayBuffer normalsBuffer;
	private ArrayBuffer textureCoordsBuffer;
	private ArrayElementsBuffer indicesBuffer;
	
	private int positionHandle;
	private int normalsHandle;
	private int textureCoordHandle;
	
	int[] vaoHandle;
	
	public Model() {}
	
	abstract public void load();
	
	public void init(GL4 gl, int positionHandle) {
		
		init(gl, positionHandle, -1, null, -1);
	}
	
	public void init(GL4 gl, int positionHandle, int normalsHandle) {
		
		init(gl, positionHandle, normalsHandle, null, -1);
	}
	
	public void init(GL4 gl, int positionHandle,
			Texture2D texture, int textureCoordHandle) {
		
		init(gl, positionHandle, -1, texture, textureCoordHandle);
	}
	
	public void init(GL4 gl, int positionHandle, int normalsHandle, 
			Texture2D texture, int textureCoordHandle) {
		
		this.gl = gl;
		
		load();
		
		this.positionHandle = positionHandle;
		this.normalsHandle = normalsHandle;
		
		this.texture = texture;
		this.textureCoordHandle = textureCoordHandle;
		
		initBuffers();
	}
	
	private void initBuffers() {
		
		if (positions != null) {
			positionsBuffer = new ArrayBuffer(gl, positions, 3, positionHandle);
		}
		
		if (normals != null && normalsHandle >= 0) {
			normalsBuffer = new ArrayBuffer(gl, normals, 3, normalsHandle);
		}
		
		if (textureCoord != null && textureCoordHandle >= 0) {
			textureCoordsBuffer = new ArrayBuffer(gl, textureCoord, 2, textureCoordHandle);
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
		
		if (textureCoordsBuffer != null) {
			textureCoordsBuffer.bind();
			texture.bind();
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
