package br.usp.gl.app;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GL;
import javax.media.opengl.GL4;
import javax.media.opengl.GLAutoDrawable;

import br.usp.gl.core.GLOrthoApp;
import br.usp.gl.core.JsonModel;
import br.usp.gl.core.Light;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;


public class AutonormApp extends GLOrthoApp implements KeyListener {

	private static final int FPS = 60;

	private Light light;
	
	private float tessInnerLevel;
	private float tessOuterLevel;
	
	private int tessInnerLevelHandle = -1;
	private int tessOuterLevelHandle = -1;
	
	private int pancakeHandle = -1;
	private boolean wireframe = false;
	private boolean pancake = false;
	
	private static final String shadersFolder = "br/usp/gl/app/shaders/autonorm/";
	
	private JsonModel model;
	private static final String modelFilePath = 
			"/home/fm/workspace/pf/data/43157/1000/cut3/projections/funnel.json";
//			"/home/fm/jsws/demos/lesson14/Teapot.json";
	
	public AutonormApp() {
		
		super(new String[] {
				shadersFolder + "vertex.glsl", 
				"", 
				"", 
				shadersFolder + "geometry.glsl",
				shadersFolder + "fragment.glsl"
		});

		this.glCanvas.addKeyListener(this);
		
		model = new JsonModel(modelFilePath);
		
		light = new Light(
				new float[]{1.0f, 1.0f, 1.0f},
				new float[]{0.1f, 0.1f, 0.1f, 1.0f},
				new float[]{0.5f, 0.5f, 0.5f, 1.0f},
				new float[]{0.7f, 0.7f, 0.7f, 1.0f}, true);
		
		tessInnerLevel = 1;
		tessOuterLevel = 1;
	}

	@Override
	public void init(final GLAutoDrawable drawable) {

		gl.glClearColor(0.7f, 0.6f, 0.5f, 0.0f);
		
		gl.glEnable(GL4.GL_DEPTH_TEST);
		
		gl.glClearDepth(1.0f);
		
		light.init(gl, shaderProgram.getUniformLocation("uLightDirection"),
				shaderProgram.getUniformLocation("uLightAmbientColor"),
				shaderProgram.getUniformLocation("uLightDiffuseColor"),
				shaderProgram.getUniformLocation("uLightSpecularColor"));
		
		tessInnerLevelHandle = shaderProgram.getUniformLocation("uTessInnerLevel");
		tessOuterLevelHandle = shaderProgram.getUniformLocation("uTessOuterLevel");
		
		pancakeHandle = shaderProgram.getUniformLocation("uPancake");
		
		model.init(gl, shaderProgram.getAttribLocation("aVertexPosition"),
				shaderProgram.getAttribLocation("aVertexNormal"));
		model.bind();
	}

	@Override
	public void display(final GLAutoDrawable drawable) {

		gl.glUniform1f(tessInnerLevelHandle, tessInnerLevel);
	    gl.glUniform1f(tessOuterLevelHandle, tessOuterLevel);
	    
	    light.bindDirection();
	    light.bindAmbientColor();
	    light.bindDiffuseColor();
	    light.bindSpecularColor();
	    
	    if (wireframe) {
	    	gl.glPolygonMode(GL4.GL_FRONT_AND_BACK, GL4.GL_LINE);
	    } else {
	    	gl.glPolygonMode(GL4.GL_FRONT_AND_BACK, GL4.GL_FILL);
	    }
	    
	    gl.glUniform1i(pancakeHandle, (pancake) ? 1 : 0);
	    
	    mvMatrix.bind();
	    
	    nMatrix.update(mvMatrix);
	    nMatrix.bind();
	    
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		gl.glPatchParameteri(GL4.GL_PATCH_VERTICES, 3);
		
	    model.draw(GL4.GL_TRIANGLES);
	}

	@Override
	public void reshape(final GLAutoDrawable drawable, final int x,
			final int y, final int width, final int height) {}

	@Override
	public void dispose(final GLAutoDrawable drawable) {
		
		model.dispose();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode())
		{
		case 33: // page up
			break;
			
		case 34: // page down
			break;
			
		case 37: // left arrow
			tessInnerLevel -= (tessInnerLevel > 1) ? 1 : 0;
			break;
			
		case 38: // up arrow
			tessOuterLevel++;
			break;
			
		case 39: // right arrow
			tessInnerLevel++;
			break;
			
		case 40: // down arrow
			tessOuterLevel -= (tessOuterLevel > 1) ? 1 : 0;
			break;
			
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		switch (e.getKeyChar()) {
		case 'w':
			wireframe = !wireframe;
			break;
			
		case 'p':
			pancake = !pancake;
			break;

		default:
			break;
		}
		
	}

	public static void main(final String args[]) {

		AutonormApp app = new AutonormApp();

		Frame frame = new Frame("OpenGL 4 ");
		frame.add(app.getGLCanvas());
		frame.setSize(app.getGLCanvas().getWidth(), app.getGLCanvas().getHeight());

		final AnimatorBase animator;
		int fps = FPS;
		if (fps < 0) {
			 animator = new Animator(app.getGLCanvas());
		} else {
			 animator = new FPSAnimator(app.getGLCanvas(), fps);
		}
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				new Thread(new Runnable() {
					public void run() {
						animator.stop();
						System.exit(0);
					}
				}).start();
			}
		});
		frame.setVisible(true);
		app.getGLCanvas().requestFocusInWindow();

		animator.start();
	}
}