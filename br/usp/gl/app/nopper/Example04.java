package br.usp.gl.app.nopper;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;

import br.usp.gl.core.GLOrthoApp;
import br.usp.gl.core.Light;
import br.usp.gl.models.Cube;
import br.usp.gl.models.Model;
import br.usp.gl.util.Maths;


public class Example04 extends GLOrthoApp{

	public static final int FPS = 60;
	public static final String SHADERS_FOLDER = "shaders/nopper/c/";
	public static final String TEXTURES_FOLDER = "data/textures/";
	
	private Light light;

	private Model model;
	
	public Example04() {
		
		super(SHADERS_FOLDER);
		
		light = new Light(
				new float[]{1.0f, 1.0f, 1.0f},
				new float[]{0.0f, 0.0f, 0.0f, 1.0f},
				new float[]{1.0f, 0.0f, 0.0f, 1.0f},
				new float[]{1.0f, 1.0f, 1.0f, 1.0f}, true);

		model = new Cube();
		
	}

	@Override
	public void init() {

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepth(1.0f);
		
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_CULL_FACE);
		
		light.init(gl, shaderProgram.getUniformLocation("u_lightDirection"),
				shaderProgram.getUniformLocation("uLightAmbientColor"),
				shaderProgram.getUniformLocation("u_color"),
				shaderProgram.getUniformLocation("uLightSpecularColor"));
		
		model.init(gl, shaderProgram.getAttribLocation("a_vertex"),
				shaderProgram.getAttribLocation("a_normal"));
	}

	@Override
	public void display() {

		gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
		
		light.bind();
		model.bind();
		
		mvMatrix.rotate(Maths.degToRad(45), new float[]{1,0,0});
		mvMatrix.rotate(Maths.degToRad(45), new float[]{0,1,0});
		
		mvMatrix.bind();
		
		nMatrix.update(mvMatrix);
		nMatrix.bind();
			
		model.draw(GL3.GL_TRIANGLES);
		
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