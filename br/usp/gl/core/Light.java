package br.usp.gl.core;

import javax.media.opengl.GL4;

import br.usp.gl.vectors.Vector3;
import br.usp.gl.vectors.Vector4;

import com.jogamp.common.nio.Buffers;

public class Light {
	
	private GL4 gl;
	
	private Vector3 direction;
	private Vector4 ambientColor;
	private Vector4 diffuseColor;
	private Vector4 specularColor;
	
	
	private int directionHandle;
	private int ambientColorHandle;
	private int diffuseColorHandle;
	private int specularColorHandle;
	
	private boolean on;
	
	private Vector3 adjustedLD;
	
	public Light(float[] direction, float[] ambientColor,
			float[] diffuseColor, float[] specularColor, boolean on) {
		
		setDirection(direction);
		setAmbientColor(ambientColor);
		setDiffuseColor(diffuseColor);
		setSpecularColor(specularColor);
		
		this.on = on;
	}

	public GL4 getGl() {return gl;}
	
	public float[] getDirection() {return direction.getVector();}
	public float[] getAmbientColor() {return ambientColor.getVector();}
	public float[] getDiffuseColor() {return diffuseColor.getVector();}
	public float[] getSpecularColor() {return specularColor.getVector();}

	public void setDirection(float[] direction) {this.direction = new Vector3(direction);}
	public void setAmbientColor(float[] ambientColor) {this.ambientColor = new Vector4(ambientColor);}
	public void setDiffuseColor(float[] diffuseColor) {this.diffuseColor = new Vector4(diffuseColor);}
	public void setSpecularColor(float[] specularColor) {this.specularColor = new Vector4(specularColor);}

	public int getDirectionHandle() {return directionHandle;}
	public int getAmbientColorHandle() {return ambientColorHandle;}
	public int getDiffuseColorHandle() {return diffuseColorHandle;}
	public int getSpecularColorHandle() {return specularColorHandle;}
	
	public float[] getAdjustedLD() {
		
		if (adjustedLD == null) {
			adjustedLD = Vector3.normalize(direction);
			adjustedLD.scale(-1);
		}
		return adjustedLD.getVector();
	}
	
	public boolean isOn() {return on;}
	public void turnOn() {this.on = true;}
	public void turnOff() {this.on = false;}
	public void switchOn() {this.on = !this.on;}

	public void init(GL4 gl, int directionHandle, int ambientColorHandle,
			int diffuseColorHandle, int specularColorHandle) {
		
		this.gl = gl;
		
		this.directionHandle = directionHandle;
		this.ambientColorHandle = ambientColorHandle;
		this.diffuseColorHandle = diffuseColorHandle;
		this.specularColorHandle = specularColorHandle;
	}
	
	public void bind() {
		
		bindDirection();
		bindAmbientColor();
		bindDiffuseColor();
		bindSpecularColor();
	}
	
	public void bindDirection() {
		
		float[] normalizedDir = Vector3.normalize(direction).getVector();
		gl.glUniform3fv(directionHandle, 1, Buffers.newDirectFloatBuffer(normalizedDir));
	}
	
	public void bindAdjustedLD() {
		gl.glUniform3fv(directionHandle, 1, Buffers.newDirectFloatBuffer(getAdjustedLD()));
	}
	
	public void bindAmbientColor() {
		gl.glUniform4fv(ambientColorHandle, 1, Buffers.newDirectFloatBuffer(getAmbientColor()));
	}
	
	public void bindDiffuseColor() {
		gl.glUniform4fv(diffuseColorHandle, 1, Buffers.newDirectFloatBuffer(getDiffuseColor()));
	}
	
	public void bindSpecularColor() {
		gl.glUniform4fv(specularColorHandle, 1, Buffers.newDirectFloatBuffer(getSpecularColor()));
	}
}
