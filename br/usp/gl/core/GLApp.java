package br.usp.gl.core;

import java.util.Calendar;

import javax.media.opengl.GL;
import javax.media.opengl.GL4;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import br.usp.gl.matrices.ModelViewMatrix;
import br.usp.gl.matrices.NormalMatrix;
import br.usp.gl.matrices.ProjectionMatrix;
import br.usp.gl.shaders.ShaderProgram;


public abstract class GLApp implements GLEventListener {

	// OpenGL Pipeline Object
	protected GL4 gl;
	
	// Constants
	public static final int INITIAL_WINDOW_WIDTH = 800;
	public static final int INITIAL_WINDOW_HEIGTH = 600;
	
	// GLCanvas
	protected GLCanvas glCanvas;
	protected int canvasWidth;
	protected int canvasHeight;
	protected float aspect;
	
	// Shader Program
	protected final ShaderProgram shaderProgram;
	
	// Transformation Matrices
	protected ProjectionMatrix pMatrix;
	protected ModelViewMatrix mvMatrix;
	
	// Normal Matrix
	protected NormalMatrix nMatrix;
	
	// Misc
	protected long lastTime;
	
	public GLApp(String shadersFolder) {

		System.setProperty( "java.library.path", "/path/to/libs" );
		
		GLProfile profile = GLProfile.getDefault();

		GLCapabilities glcaps = new GLCapabilities(profile);
		glcaps.setAccumBlueBits(16);
		glcaps.setAccumGreenBits(16);
		glcaps.setAccumRedBits(16);
		glcaps.setDoubleBuffered(true);
		glcaps.setHardwareAccelerated(true);
		
		this.canvasWidth = INITIAL_WINDOW_WIDTH;
		this.canvasHeight = INITIAL_WINDOW_HEIGTH;
		
		glCanvas = new GLCanvas(glcaps);
		glCanvas.setSize(canvasWidth, canvasHeight);
		
		glCanvas.addGLEventListener(0, new BasicEvents());
		glCanvas.addGLEventListener(1, this);
		
		shaderProgram = new ShaderProgram(shadersFolder);
		
		pMatrix = new ProjectionMatrix();
		mvMatrix = new ModelViewMatrix();
		nMatrix = new NormalMatrix(); 
		
		lastTime = Calendar.getInstance().getTimeInMillis();
	}

	public abstract void init(GLAutoDrawable drawable);
	public abstract void display(GLAutoDrawable drawable);
	public abstract void reshape(GLAutoDrawable drawable, int x, int y, int width, int height);
	public abstract void dispose(final GLAutoDrawable drawable);
	
	public GLCanvas getGLCanvas() {return glCanvas;}
	public void setGlCanvas(GLCanvas glCanvas) {this.glCanvas = glCanvas;}
	
	class BasicEvents implements GLEventListener {

		@Override
		public void init(GLAutoDrawable drawable) {
			
			gl = drawable.getGL().getGL4();
			
			System.out.println("OpenGL Version: " + gl.glGetString(GL.GL_VERSION) + "\n");

			shaderProgram.init(gl);
			shaderProgram.bind();
			
			pMatrix.init(gl, shaderProgram.getUniformLocation("uPMatrix"));
			mvMatrix.init(gl, shaderProgram.getUniformLocation("uMVMatrix"));
			nMatrix.init(gl, shaderProgram.getUniformLocation("uNMatrix"));
		}

		@Override
		public void dispose(GLAutoDrawable drawable) {
			
			shaderProgram.dispose();
			
		}

		@Override
		public void display(GLAutoDrawable drawable) {
			
		}

		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int width,
				int height) {
			
			gl.glViewport(0, 0, width, height); 
			
			pMatrix.loadIdentity();
			pMatrix.perspective(x, y, width, height);
			pMatrix.bind();
		}
		
	}
}
