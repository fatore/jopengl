package br.usp.gl.effects;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.media.opengl.GL;
import javax.media.opengl.GL4;

import com.jogamp.common.util.IOUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;

public class CubeMap extends Effect {

	private static final String[] suffixes = { "posx", "negx", "posy", "negy", "posz", "negz" };
	private static final int[] targets = { 
		GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X,
		GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
		GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
		GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
		GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
		GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z };
	
	private Texture texture;
	
	private String basename;
	private String suffix;
	
	private boolean mipmapped;
	
	private int id;
	private int no;
	
	public CubeMap(String basename, String suffix, boolean mipmapped, int id, int no) {
		
		this.basename = basename;
		this.suffix = suffix;
		this.mipmapped = mipmapped;
		
		this.id = id;
		this.no = no;
	}
	
	@Override
	public void init(GL4 gl, int handle)  {
		
		super.init(gl, handle);
		
		texture = TextureIO.newTexture(GL.GL_TEXTURE_CUBE_MAP);
		texture.bind(gl);

		try {
			for (int i = 0; i < suffixes.length; i++) {
				
				String resourceName = basename + suffixes[i] + "." + suffix;
				
				TextureData data = TextureIO.newTextureData(gl.getGLProfile(), 
						new FileInputStream(new File(resourceName)),
						mipmapped,
						IOUtil.getFileSuffix(resourceName));
				if (data == null) {
					throw new IOException("Unable to load texture " + resourceName);
				}
				texture.updateImage(gl, data, targets[i]);
			}
		} catch (Exception e) {
			System.err.println("Failed to load textures.");
			e.printStackTrace();
		}
		
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR_MIPMAP_NEAREST);
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_S, GL4.GL_REPEAT);
		gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_T, GL4.GL_REPEAT);
	}
	
	@Override
	public void bind() {
		
		gl.glEnable(GL4.GL_TEXTURE_CUBE_MAP);

		gl.glUniform1i(handle, no);
		gl.glActiveTexture(id);
		texture.bind(gl);		
		texture.enable(gl);
	}
}
