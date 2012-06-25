package br.usp.gl.core;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

import br.usp.gl.listeners.PanListener;
import br.usp.gl.listeners.TrackballListener;
import br.usp.gl.listeners.ZoomListener;
import br.usp.gl.matrices.Matrix3;
import br.usp.gl.matrices.Matrix4;


public abstract class GLOrthoApp extends GLApp {

	public static final float INITIAL_ZOOM = 2;
	
	// Canvas Listeners
	protected PanListener panListener;
	protected ZoomListener zoomListener;
	protected TrackballListener trackballListener;
	
	// Matrices
	protected Matrix4 mMatrix;
	protected Matrix4 vMatrix;
	protected Matrix4 pMatrix;
	
	// Normal Matrices
	protected Matrix3 nMatrix;
	
	protected Matrix4 mvMatrix;

	// Rotation Matrix
	private Matrix4 rotationMatrix;
	
	public GLOrthoApp(String shadersFolder) {
		
		super(shadersFolder);
		
		mMatrix = new Matrix4();
		vMatrix = new Matrix4();
		pMatrix = new Matrix4();
		
		mvMatrix = new Matrix4();
		
		nMatrix = new Matrix3();
		
		// Events Listeners
		glCanvas.addGLEventListener(new OrthoEventsListener());

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
			
			pMatrix.init(gl, shaderProgram.getUniformLocation("u_projectionMatrix"));
			
			mvMatrix.init(gl, shaderProgram.getUniformLocation("u_modelViewMatrix"));
			
			nMatrix.init(gl, shaderProgram.getUniformLocation("u_normalMatrix"));
			
			// Create the model matrix.
			mMatrix.loadIdentity();
			
			// Create the view matrix.
			vMatrix.loadIdentity();
			vMatrix.lookAt(0.0f, 0.0f, 5.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
			
			// MV = V * M
			mvMatrix.loadIdentity();
			mvMatrix.multiply(vMatrix, mMatrix);
			mvMatrix.bind();
		}

		@Override
		public void display(GLAutoDrawable drawable) {
			
			mvMatrix.loadIdentity();

			rotationMatrix = trackballListener.getRotationMatrix();
			if (rotationMatrix != null) {
				mvMatrix.multiply(rotationMatrix);
			}
			mvMatrix.bind();
			
			nMatrix.extract(mvMatrix);
		    nMatrix.bind();
			
			updateZoomAndPan();
		}
		
		@Override
		public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
			
			if (h == 0) {h = 1;}
			
			canvasWidth = w; 
			canvasHeight = h;
			
			aspect = (float) canvasWidth / canvasHeight;
			
			gl.glViewport(0, 0, canvasWidth, canvasHeight);  
			
			updateZoomAndPan();
		}
		
		@Override
		public void dispose(GLAutoDrawable drawable) {
		}
		
		private void updateZoomAndPan() {
			
			// Create a orthogonal projection matrix.
			pMatrix.loadIdentity();
//			projectionMatrix.ortho(
//					-(float) width / 2.0f, (float) width / 2.0f,
//					-(float) height / 2.0f, (float) height / 2.0f,
//					-1.0f, 100.0f);
			
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
