package br.usp.gl.core;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import br.usp.gl.listeners.PanListener;
import br.usp.gl.listeners.TrackballListener;
import br.usp.gl.listeners.ZoomListener;
import br.usp.gl.matrices.Matrix4;


public abstract class GLOrthoApp extends GLApp {

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
			mvMatrix.bind();
			
			nMatrix.update(mvMatrix);
			nMatrix.bind();

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
