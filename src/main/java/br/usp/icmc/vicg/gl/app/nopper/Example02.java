package br.usp.icmc.vicg.gl.app.nopper;

import javax.media.opengl.GL3;

import br.usp.icmc.vicg.gl.core.GLApp;
import br.usp.icmc.vicg.gl.models.Model;
import br.usp.icmc.vicg.gl.models.Triangle;
import br.usp.icmc.vicg.gl.shaders.SingleShaderProgram;


public class Example02 extends GLApp {

	public static final int FPS = 60;
	public static final String SHADERS_FOLDER = "resources/shaders/nopper/2/";
	
	private SingleShaderProgram shaderProgram;
	
	private Model model;
	
	public Example02() {
		shaderProgram = new SingleShaderProgram(SHADERS_FOLDER);
		model = new Triangle();
	}

	@Override
	public void init() {
		shaderProgram.init(gl);
		shaderProgram.bind();

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		model.init(gl, shaderProgram.getAttribLocation("a_position"));
		model.bind();
	}

	@Override
	public void display() {
		gl.glClear(GL3.GL_COLOR_BUFFER_BIT);
		
		model.draw(GL3.GL_TRIANGLES);
		
		gl.glFlush();
	}

	@Override
	public void reshape(final int x, final int y, final int width, final int height) {
		gl.glViewport(0, 0, canvasWidth, canvasHeight); 
	}

	@Override
	public void dispose() {
		model.dispose();
	}
	
	public static void main(final String args[]) {
		Example02 app = new Example02();
		app.run(app.getClass().getName(), FPS);
	}
}