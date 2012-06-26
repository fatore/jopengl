package br.usp.gl.core;

import javax.media.opengl.GL3;

import br.usp.gl.vectors.Vector3;
import br.usp.gl.vectors.Vector4;

import com.jogamp.common.nio.Buffers;

public class Light {
	
	private GL3 gl;
	
	private Vector3 direction;
	private Vector4 ambientColor;
	private Vector4 diffuseColor;
	private Vector4 specularColor;
	
	private int directionHandle;
	private int ambientColorHandle;
	private int diffuseColorHandle;
	private int specularColorHandle;
	
	private boolean on;
	
	public Light(float[] direction, float[] ambientColor,
			float[] diffuseColor, float[] specularColor, boolean on) {
		
		setDirection(direction);
		setAmbientColor(ambientColor);
		setDiffuseColor(diffuseColor);
		setSpecularColor(specularColor);
		
		this.on = on;
	}

	public float[] getDirection() {return direction.getVector();}
	public float[] getAmbientColor() {return ambientColor.getVector();}
	public float[] getDiffuseColor() {return diffuseColor.getVector();}
	public float[] getSpecularColor() {return specularColor.getVector();}
	
	public void incDirection(float[] inc) {
		
		this.direction.getVector()[0] += inc[0];
		this.direction.getVector()[1] += inc[1];
		this.direction.getVector()[2] += inc[2];
		
		this.direction.normalize();
	}

	public void setDirection(float[] direction) {
		this.direction = new Vector3(direction);
		this.direction.normalize();
	}
	public void setAmbientColor(float[] ambientColor) {this.ambientColor = new Vector4(ambientColor);}
	public void setDiffuseColor(float[] diffuseColor) {this.diffuseColor = new Vector4(diffuseColor);}
	public void setSpecularColor(float[] specularColor) {this.specularColor = new Vector4(specularColor);}

	public int getDirectionHandle() {return directionHandle;}
	public int getAmbientColorHandle() {return ambientColorHandle;}
	public int getDiffuseColorHandle() {return diffuseColorHandle;}
	public int getSpecularColorHandle() {return specularColorHandle;}
	
	public boolean isOn() {return on;}
	public void turnOn() {this.on = true;}
	public void turnOff() {this.on = false;}
	public void switchOn() {this.on = !this.on;}

	public void init(GL3 gl, int directionHandle, int ambientColorHandle,
			int diffuseColorHandle, int specularColorHandle) {
		
		this.gl = gl;
		
		this.directionHandle = directionHandle;
		this.ambientColorHandle = ambientColorHandle;
		this.diffuseColorHandle = diffuseColorHandle;
		this.specularColorHandle = specularColorHandle;
	}
	
	public void bind() {
		
		gl.glUniform3fv(directionHandle, 1, Buffers.newDirectFloatBuffer(getDirection()));
		gl.glUniform4fv(ambientColorHandle, 1, Buffers.newDirectFloatBuffer(getAmbientColor()));
		gl.glUniform4fv(diffuseColorHandle, 1, Buffers.newDirectFloatBuffer(getDiffuseColor()));
		gl.glUniform4fv(specularColorHandle, 1, Buffers.newDirectFloatBuffer(getSpecularColor()));
	}
}
