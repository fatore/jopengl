package br.usp.gl.app.nopper;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;

import br.usp.gl.core.GLApp;
import br.usp.gl.core.Light;
import br.usp.gl.core.Material;
import br.usp.gl.matrices.Matrix4;
import br.usp.gl.models.Model;
import br.usp.gl.models.Sphere;


public class Example05 extends GLApp {

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

		gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
		
		mvMatrix.lookAt(0.0f, 0.0f, 5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
		mvMatrix.bind();
		
		nMatrix.extractMatrix(mvMatrix);
		nMatrix.bind();
		
		light.setDirection(Matrix4.multiplyVector3(mvMatrix.getMatrix(), light.getDiffuseColor()));
		
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

		Example05 app = new Example05();
		app.run(app.getClass().getName(), FPS);
	}
}