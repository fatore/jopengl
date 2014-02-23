package br.usp.icmc.vicg.gl.app.nopper;

import java.util.Calendar;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;

import br.usp.icmc.vicg.gl.core.GLApp;
import br.usp.icmc.vicg.gl.core.Light;
import br.usp.icmc.vicg.gl.core.Material;
import br.usp.icmc.vicg.gl.effects.Texture2D;
import br.usp.icmc.vicg.gl.matrices.Matrix3;
import br.usp.icmc.vicg.gl.matrices.Matrix4;
import br.usp.icmc.vicg.gl.models.Cube;
import br.usp.icmc.vicg.gl.models.Model;
import br.usp.icmc.vicg.gl.shaders.SingleShaderProgram;
import br.usp.icmc.vicg.gl.util.Maths;


public class Example06 extends GLApp {

	public static final int FPS = 60;
	public static final String SHADERS_FOLDER = "resources/shaders/nopper/6/";
	public static final String TEXTURES_FOLDER = "resources/textures/";
	public static final String MODELS_FOLDER = "resources/models/";
	
	private SingleShaderProgram shaderProgram;
	
	private Matrix4 modelMatrix;
	private Matrix4 viewMatrix;
	private Matrix4 projectionMatrix;
	private Matrix4 viewProjectionMatrix;
	
	private Matrix3 normalMatrix;
	
	private Light light;
	
	private Material material;

	private Texture2D texture;
	
	private Model model;
	
	private float angle = 0.0f;
	
	public Example06() {
		shaderProgram = new SingleShaderProgram(SHADERS_FOLDER);
		
		modelMatrix = new Matrix4();
		viewMatrix = new Matrix4();
		projectionMatrix = new Matrix4();
		viewProjectionMatrix = new Matrix4();
		normalMatrix = new Matrix3();
		
		light = new Light(
				new float[]{1.0f, 1.0f, 1.0f},
				new float[]{0.3f, 0.3f, 0.3f, 1.0f},
				new float[]{1.0f, 1.0f, 1.0f, 1.0f},
				new float[]{1.0f, 1.0f, 1.0f, 1.0f}, true);

		material = new Material(
				new float[]{0.0f, 0.0f, 1.0f, 1.0f},
				new float[]{0.0f, 0.0f, 1.0f, 1.0f},
				new float[]{1.0f, 1.0f, 1.0f, 1.0f}, 20.0f);
		
		texture = new Texture2D(TEXTURES_FOLDER + "crate.png", GL3.GL_TEXTURE0, 0);
		
		model = new Cube(0.5f);
	}

	@Override
	public void init() {
		shaderProgram.init(gl);
		shaderProgram.bind();

		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glClearDepth(1.0f);
		
		gl.glEnable(GL.GL_DEPTH_TEST);
		gl.glEnable(GL.GL_CULL_FACE);
		
		viewProjectionMatrix.init(gl, shaderProgram.getUniformLocation("u_viewProjectionMatrix"));
		modelMatrix.init(gl, shaderProgram.getUniformLocation("u_modelMatrix"));
		normalMatrix.init(gl, shaderProgram.getUniformLocation("u_normalMatrix"));
		
		light.init(gl, shaderProgram.getUniformLocation("u_lightDirection"),
				shaderProgram.getUniformLocation("u_light.ambientColor"),
				shaderProgram.getUniformLocation("u_light.diffuseColor"),
				shaderProgram.getUniformLocation("u_light.specularColor"));
		
		material.init(gl, shaderProgram.getUniformLocation("u_material.ambientColor"),
				shaderProgram.getUniformLocation("u_material.diffuseColor"),
				shaderProgram.getUniformLocation("u_material.specularColor"),
				shaderProgram.getUniformLocation("u_material.specularExponent"));
		
		texture.init(gl, shaderProgram.getUniformLocation("u_texture"));
		
		model.init(gl, shaderProgram.getAttribLocation("a_position"),
				shaderProgram.getAttribLocation("a_normal"), 
				texture, shaderProgram.getAttribLocation("a_texCoord"));
	}

	@Override
	public void display() {
		gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
		
		modelMatrix.loadIdentity();
		modelMatrix.rotate(Maths.degToRad(45), new float[]{1,0,0});
		modelMatrix.rotate(Maths.degToRad(angle), new float[]{0,1,0});
		modelMatrix.bind();
		
		// Model matrix is a rigid body matrix.
		normalMatrix.extract(modelMatrix);
		normalMatrix.bind();
		
		light.bind();
		material.bind();
		texture.bind();
		
		model.bind();
		model.draw(GL3.GL_TRIANGLES);
		
		gl.glFlush();
		
		update();
	}
	
	private void update() {
		long currentTime = Calendar.getInstance().getTimeInMillis();
		long elapsedTime = currentTime - lastTime;
		
		angle += 90.0f * (elapsedTime / 1000.0f);
		
		lastTime = currentTime;
	}

	@Override
	public void reshape(final int x, final int y, final int width, final int height) {
		gl.glViewport(0, 0, width, height);
		
		// View Matrix
		viewMatrix.loadIdentity();
		viewMatrix.lookAt(0.0f, 0.0f, 5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
		
		// Projection Matrix
		projectionMatrix.loadIdentity();
		projectionMatrix.perspective(40f, aspect, 1.0f, 100.0f);
		
		// VP = P * V
		viewProjectionMatrix.loadIdentity();
		viewProjectionMatrix.multiply(projectionMatrix, viewMatrix);
		
		viewProjectionMatrix.bind();
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