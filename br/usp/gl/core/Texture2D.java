package br.usp.gl.core;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.media.opengl.GL4;

import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class Texture2D {
	
	protected GL4 gl;
	
	private int id;
	private int no;
	
	private BufferedImage image;
	
	private int handle;
	
	private Texture texture;
	
	
	public Texture2D(String imageFile, int id, int no) {
		
		try {
			image = ImageIO.read(new File(imageFile)); 
			ImageUtil.flipImageVertically(image); 
			
		} catch (Exception e) {
			System.err.println("Failed to load texture: " + imageFile);
			e.printStackTrace();
		}
		
		this.id = id;
		this.no = no;
	}
	
	public void init(GL4 gl, int handle) {
		
		this.gl = gl;
		
		this.handle = handle;
		
		texture = AWTTextureIO.newTexture(gl.getGLProfile(), image, true);
		
		texture.bind(gl);
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_NEAREST);
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_NEAREST);
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_S, GL4.GL_REPEAT);
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_T, GL4.GL_REPEAT);
		
	}
	
	public void bind() {
		
		gl.glActiveTexture(id);
		texture.bind(gl);		
		gl.glUniform1i(handle, no);
	}

}
