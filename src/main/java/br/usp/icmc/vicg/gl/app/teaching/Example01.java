package br.usp.icmc.vicg.gl.app.teaching;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.GL3;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.util.AnimatorBase;
import com.jogamp.opengl.util.FPSAnimator;

public class Example01 implements GLEventListener {

    // OpenGL Pipeline Object
    private GL3 gl;

    // GLCanvas
    private GLCanvas glCanvas;

    // Triangle
    private static final float[] TIRANGLE_VERTICES = new float[] {
        -0.5f, 0.0f, 0.0f, 
        0.5f, 0.0f, 0.0f, 
        0.0f, 0.5f, 0.0f,
    };

    // Vertex Buffer Object
    private int[] vbo;

    public Example01() {
        // Get profile
        GLProfile profile = GLProfile.getDefault();

        // Configurations
        GLCapabilities glcaps = new GLCapabilities(profile);
        glcaps.setDoubleBuffered(true);
        glcaps.setHardwareAccelerated(true);

        // Create canvas
        glCanvas = new GLCanvas(glcaps);
        glCanvas.setSize(400, 300);

        // Add listener to panel
        glCanvas.addGLEventListener(this);
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        // *****************************************
        // ********** BASIC CONFIGURATION **********
        // *****************************************
        
        gl = drawable.getGL().getGL3();

        // debugging
        System.out.println("OpenGL Version: " + gl.glGetString(GL.GL_VERSION) + "\n");

        // set background color
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

        // *****************************************
        // ********** SHADER CONFIGURATION *********
        // *****************************************
        
        // Read vertex shader source
        String vertexShaderFile = "resources/shaders/teaching/1/vertex.glsl";
        String vertexShaderSource = null;
        try {
            vertexShaderSource = readSource(new FileInputStream(new File(vertexShaderFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Compile vertex shader
        int vertexShader = compileSource(vertexShaderSource, GL3.GL_VERTEX_SHADER);

        // Read fragment shader source
        String fragmentShaderFile = "resources/shaders/teaching/1/fragment.glsl";
        String fragmentShaderSource = null;
        try {
            fragmentShaderSource = readSource(new FileInputStream(new File(fragmentShaderFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Compile vertex shader
        int fragmentShader = compileSource(fragmentShaderSource, GL3.GL_FRAGMENT_SHADER);

        // Link shaders
        int programHandle = linkShaders(new int[]{vertexShader, fragmentShader});

        // dispose temporary data
        gl.glDeleteShader(vertexShader);
        gl.glDeleteShader(fragmentShader);

        // pass shader program to the pipeline
        gl.glUseProgram(programHandle);

        // *****************************************
        // ********** BUFFER CONFIGURATION *********
        // *****************************************
        
        // create vertex positions buffer
        vbo = new int[1];
        gl.glGenBuffers(1, vbo, 0);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]);
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, TIRANGLE_VERTICES.length * Buffers.SIZEOF_FLOAT,
                Buffers.newDirectFloatBuffer(TIRANGLE_VERTICES), GL3.GL_STATIC_DRAW);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);

        // pass buffer to shader program
        int vertexPosition = gl.glGetAttribLocation(programHandle, "a_position");
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo[0]);
        gl.glVertexAttribPointer(vertexPosition, 3, GL3.GL_FLOAT, false, 0, 0);
        gl.glEnableVertexAttribArray(vertexPosition);
    }


    @Override
    public void display(GLAutoDrawable drawable) {
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT);
        
        gl.glDrawArrays(GL3.GL_TRIANGLES, 0, TIRANGLE_VERTICES.length / 3);
        
        gl.glFlush();
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {}

    @Override
    public void dispose(GLAutoDrawable drawable) {
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
        if (vbo[0] > 0) {
            gl.glDeleteBuffers(1, vbo, 0);
            vbo[0] = 0;
        }        
    }

    private String readSource(final InputStream inputStream) {
        final StringBuilder source = new StringBuilder();
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null)
                source.append(line + "\n");

            bufferedReader.close();
        } catch (final IOException e) {
            System.err.println("Invalid shader file.");
        }
        return source.toString();
    }

    private int compileSource(final String source, final int type) {
        final IntBuffer buffer = Buffers.newDirectIntBuffer(1);
        final int handle = gl.glCreateShader(type);

        gl.glShaderSource(handle, 1, new String[] { source }, null, 0);
        gl.glCompileShader(handle);

        gl.glGetShaderiv(handle, GL3.GL_COMPILE_STATUS, buffer);

        if (buffer.get(0) == 1)
            return handle;
        else {
            gl.glGetShaderiv(handle, GL3.GL_INFO_LOG_LENGTH, buffer);

            final ByteBuffer byteBuffer = Buffers.newDirectByteBuffer(buffer
                    .get(0));
            gl.glGetShaderInfoLog(handle, byteBuffer.capacity(), buffer,
                    byteBuffer);

            System.err.println("\nshader compile error: ");
            for (int i = 0; i < buffer.get(0); i++) {
                System.err.print((char) byteBuffer.get(i));
            }

            return -1;
        }
    }

    private int linkShaders(int[] shadersHandles) {
        final int programHandle = gl.glCreateProgram();

        for (int i = 0; i < shadersHandles.length; i++) {
            if (shadersHandles[i] >= 0) {
                gl.glAttachShader(programHandle, shadersHandles[i]);
            }
        }

        gl.glLinkProgram(programHandle);
        gl.glValidateProgram(programHandle);

        final IntBuffer buffer = Buffers.newDirectIntBuffer(1);
        gl.glGetProgramiv(programHandle, GL3.GL_VALIDATE_STATUS, buffer);

        if (buffer.get(0) == 1)
            return programHandle;
        else {

            gl.glGetProgramiv(programHandle, GL3.GL_INFO_LOG_LENGTH, buffer);

            final ByteBuffer byteBuffer = Buffers.newDirectByteBuffer(buffer
                    .get(0));
            gl.glGetProgramInfoLog(programHandle, byteBuffer.capacity(), buffer,
                    byteBuffer);

            System.err.println("\nshader link error: ");
            for (int i = 0; i < buffer.get(0); i++) {
                System.err.print((char) byteBuffer.get(i));
            }

            return -1;
        }
    }

    public void run(String title, int fps) {
        Frame frame = new Frame(title);
        frame.add(this.glCanvas);
        frame.pack();
        final AnimatorBase animator = new FPSAnimator(this.glCanvas, fps);

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
        animator.start();
    }

    public static void main(String[] args) {
        // Run example
        Example01 example01 = new Example01();
        example01.run("Example 01", 60);
    }
}
