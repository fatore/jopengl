package br.usp.gl.util;

import java.io.InputStream;


public class Resource {

    public static InputStream getInputStream(final String path) {

        final InputStream inputStream = Resource.class.getClassLoader().getResourceAsStream(path);

        if (inputStream == null) throw new RuntimeException("file not found: " + path);
        else return inputStream;
    }
}
