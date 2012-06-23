package br.usp.gl.app.nopper;

import javax.media.opengl.GL;
import javax.media.opengl.GL4;

import br.usp.gl.core.GLOrthoApp;
import br.usp.gl.core.Light;
import br.usp.gl.core.Material;
import br.usp.gl.models.Model;
import br.usp.gl.models.Sphere;


public class Example05 extends GLOrthoApp{

	public static final int FPS = 60;
	public static final String SHADERS_FOLDER = "shaders/nopper/d/";
	public static final String TEXTURES_FOLDER = "data/textures/";
	
	private Light light;
	private Material material;

	private Model model;
	
	public Example05() {
		
		super(SHADERS_FOLDER);
		
		light = new Light(
				new float[]{1.0f, 1.0f, 1.0f},
				new float[]{0.3f, 0.3f, 0.3f, 1.0f},
				new float[]{1.0f, 1.0f, 1.0f, 1.0f},
				new float[]{1.0f, 1.0f, 1.0f, 1.0f}, true);

		material = new Material(
				new float[]{0.0f, 0.0f, 1.0f, 1.0f},
				new float[]{0.0f, 0.0f, 1.0f, 1.0f},
				new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 20.0f);
		
		model = new Sphere(0.5f, 32);
		
	}

	@Override
	public void init() {

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepth(1.0f);
		
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_CULL_FACE);
		
		light.init(gl, shaderProgram.getUniformLocation("u_light.direction"),
				shaderProgram.getUniformLocation("u_light.ambientColor"),
				shaderProgram.getUniformLocation("u_light.diffuseColor"),
				shaderProgram.getUniformLocation("u_light.specularColor"));
		
		material.init(gl, shaderProgram.getUniformLocation("u_material.ambientColor"),
				shaderProgram.getUniformLocation("u_material.diffuseColor"),
				shaderProgram.getUniformLocation("u_material.specularColor"),
				shaderProgram.getUniformLocation("u_material.specularExponent"));
		
		model.init(gl, shaderProgram.getAttribLocation("a_vertex"),
				shaderProgram.getAttribLocation("a_normal"));
	}

	@Override
	public void display() {

		gl.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
		
		light.bind();
		material.bind();
		
		model.bind();
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

		Example05 app = new Example05();
		app.run(app.getClass().getName(), FPS);
	}
}