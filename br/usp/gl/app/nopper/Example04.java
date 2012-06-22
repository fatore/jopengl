package br.usp.gl.app.nopper;

import javax.media.opengl.GL4;

import br.usp.gl.core.GLOrthoApp;
import br.usp.gl.core.Texture2D;
import br.usp.gl.models.Model;
import br.usp.gl.models.Plane;


public class Example04 extends GLOrthoApp{

	public static final int FPS = 60;
	public static final String SHADERS_FOLDER = "shaders/nopper/c/";
	public static final String TEXTURES_FOLDER = "data/textures/";
	
	private Model model;
	
	private Texture2D texture;
	
	public Example04() {
		
		super(SHADERS_FOLDER);

		texture = new Texture2D(TEXTURES_FOLDER + "desert.png", GL4.GL_TEXTURE0, 0);
		
		model = new Plane();
		
	}

	@Override
	public void init() {

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		texture.init(gl, shaderProgram.getUniformLocation("u_texture"));
		
		model.init(gl, shaderProgram.getAttribLocation("a_vertex"), 
				texture, shaderProgram.getAttribLocation("a_texCoord"));
		model.bind();
	}

	@Override
	public void display() {

		gl.glClear(GL4.GL_COLOR_BUFFER_BIT);
			
		model.draw(GL4.GL_TRIANGLES);
		
		gl.glFlush();
	}

	@Override
	public void reshape(final int x, final int y, final int width, final int height) {}

	@Override
	public void dispose() {
		
		model.dispose();
	}
	
	public static void main(final String args[]) {

		Example04 app = new Example04();
		app.run(app.getClass().getName(), FPS);
	}
}