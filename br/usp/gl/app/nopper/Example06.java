package br.usp.gl.app.nopper;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;

import br.usp.gl.core.GLApp;
import br.usp.gl.core.Light;
import br.usp.gl.core.Material;
import br.usp.gl.core.Texture2D;
import br.usp.gl.models.Cube;
import br.usp.gl.models.Model;


public class Example06 extends GLApp {

	public static final int FPS = 60;
	public static final String SHADERS_FOLDER = "shaders/nopper/e/";
	public static final String TEXTURES_FOLDER = "data/textures/";
	public static final String MODELS_FOLDER = "data/models/";
	
	private Light light;
	
	private Material material;

	private Texture2D texture;
	
	private Model model;
	
	public Example06() {
		
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
		
		texture = new Texture2D(TEXTURES_FOLDER + "crate.tga", GL3.GL_TEXTURE0, 0);
		
		model = new Cube();
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
		
		texture.init(gl, shaderProgram.getUniformLocation("u_texture"));
		
		model.init(gl, shaderProgram.getAttribLocation("a_vertex"),
				shaderProgram.getAttribLocation("a_normal"));
	}

	@Override
	public void display() {

		gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
		
		mvMatrix.loadIdentity();
		mvMatrix.lookAt(0.0f, 0.0f, 5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
		mvMatrix.bind();
		
		nMatrix.update(mvMatrix);
		nMatrix.bind();
		
		light.bind();
		material.bind();
		
		model.bind();
		model.draw(GL3.GL_TRIANGLES);
		
		gl.glFlush();
	}

	@Override
	public void reshape(final int x, final int y, final int width, final int height) {
		
		gl.glViewport(0, 0, width, height);
		
		pMatrix.loadIdentity();
		pMatrix.perspective(40f, aspect, 1.0f, 100.0f);
		pMatrix.bind();
	}

	@Override
	public void dispose() {
		
		model.dispose();
	}
	
	public static void main(final String args[]) {

		Example06 app = new Example06();
		app.run(app.getClass().getName(), FPS);
	}
}