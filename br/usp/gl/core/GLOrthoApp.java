package br.usp.gl.core;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import br.usp.gl.listeners.PanListener;
import br.usp.gl.listeners.TrackballListener;
import br.usp.gl.listeners.ZoomListener;
import br.usp.gl.matrices.Matrix4;

import com.jogamp.opengl.util.Animator;
import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;


public abstract class GLOrthoApp extends GLApp implements GLEventListener {

	public static final float INITIAL_ZOOM = 2;
	
	// Canvas Listeners
	protected PanListener panListener;
	protected ZoomListener zoomListener;
	protected TrackballListener trackballListener;

	// Rotation Matrix
	protected Matrix4 rotationMatrix;
	
	public GLOrthoApp(String shadersFolder) {
		
		super(shadersFolder);
		
		// Events Listeners
		glCanvas.addGLEventListener(0, new OrthoEventsListener());

		// Pan Listener
		panListener = new PanListener(glCanvas);
		glCanvas.addMouseListener(panListener);
		glCanvas.addMouseMotionListener(panListener);
		
		// Zoom Listener
		zoomListener = new ZoomListener(INITIAL_ZOOM);
		glCanvas.addMouseListener(zoomListener);
		glCanvas.addMouseMotionListener(zoomListener);
		
		// Track-ball Listener
		trackballListener = new TrackballListener();
		glCanvas.addMouseListener(trackballListener);
		glCanvas.addMouseMotionListener(trackballListener);
	}
	
	public void run(int fps) {
		
		Frame frame = new Frame("OpenGL 4");
		frame.add(this.getGLCanvas());
		frame.setSize(this.getGLCanvas().getWidth(), this.getGLCanvas().getHeight());

		final AnimatorBase animator;
		if (fps > 0) {
			animator = new FPSAnimator(this.getGLCanvas(), fps);
		} else {
			animator = new Animator(this.getGLCanvas());
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
		this.getGLCanvas().requestFocusInWindow();

		animator.start();
	}

	public abstract void init(GLAutoDrawable drawable);
	public abstract void display(GLAutoDrawable drawable);
	public abstract void reshape(GLAutoDrawable drawable, int x, int y, int width, int height);
	public abstract void dispose(final GLAutoDrawable drawable);
	
	class OrthoEventsListener implements GLEventListener {

		@Override
		public void init(GLAutoDrawable drawable) {
		}

		@Override
		public void display(GLAutoDrawable drawable) {
			
			mvMatrix.loadIdentity();

			rotationMatrix = trackballListener.getRotationMatrix();
			if (rotationMatrix != null) {
				mvMatrix.multiply(rotationMatrix);
			}

			defineVisualParameters();
		}
		
		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
			
			if (h == 0) {h = 1;}
			
			canvasWidth = w; 
			canvasHeight = h;
			
			aspect = (float) canvasWidth / canvasHeight;
			
			gl.glViewport(0, 0, canvasWidth, canvasHeight);  
			
			defineVisualParameters();
		}
		
		@Override
		public void dispose(GLAutoDrawable drawable) {
		}
		
		private void defineVisualParameters() {
			
			pMatrix.loadIdentity();
			
			if (canvasWidth <= canvasHeight) {
				pMatrix.ortho(
						-zoomListener.getZoom() + panListener.getPanX(), // left
					 	 zoomListener.getZoom() + panListener.getPanX(), // right
						-zoomListener.getZoom() / aspect + panListener.getPanY(), // bottom
						 zoomListener.getZoom() / aspect + panListener.getPanY(), // top
						-zoomListener.getZoom() * 10.0f, // near
						 zoomListener.getZoom() * 10.0f); // far
			} else {
				pMatrix.ortho(
						-zoomListener.getZoom() * aspect + panListener.getPanX(), // left
						 zoomListener.getZoom() * aspect + panListener.getPanX(), // right
						-zoomListener.getZoom() + panListener.getPanY(), // bottom
						 zoomListener.getZoom() + panListener.getPanY(), // top
						-zoomListener.getZoom() * 10.0f, // near
						 zoomListener.getZoom() * 10.0f); // far
			}

			pMatrix.bind();
		}
	}
}
