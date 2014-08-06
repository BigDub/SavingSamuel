package com.example.savingsamuel;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.opengl.GLES20;

public class Shader {
	private static Context _context;
	private static Shader _uniformColor, _varyingColor, _textured, _shadow, _tinted;

	
	public static void Init(Context context) {
		_context = context;
		_uniformColor = new Shader(
				_context.getString(R.string.vertexShaderAttributeP),
				_context.getString(R.string.fragmentShaderUniformC));
		_varyingColor = new Shader(
				_context.getString(R.string.vertexShaderAttributePC),
				_context.getString(R.string.fragmentShaderVaryingC));
		_textured = new Shader(
				_context.getString(R.string.vertexShaderTextured),
				_context.getString(R.string.fragmentShaderTextured));
		_shadow = new Shader(
				_context.getString(R.string.vertexShaderTextured),
				_context.getString(R.string.fragmentShaderShadow));
		_tinted = new Shader(
				_context.getString(R.string.vertexShaderTextured),
				_context.getString(R.string.fragmentShaderTinted));
	}
	private static int loadShader(int type, String shaderCode){

	    // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
	    // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
	    int shader = GLES20.glCreateShader(type);

	    // add the source code to the shader and compile it
	    GLES20.glShaderSource(shader, shaderCode);
	    GLES20.glCompileShader(shader);

	    return shader;
	}
	public static Shader UniformColor() {
		return _uniformColor;
	}
	public static Shader VaryingColor() {
		return _varyingColor;
	}
	public static Shader Textured() {
		return _textured;
	}
	public static Shader Shadow() {
		return _shadow;
	}
	public static Shader TintedTexture() {
		return _tinted;
	}
	
	private int hProgram = -1, hVertexShader = -1, hFragmentShader = -1;
	private String vertexShaderCode, fragmentShaderCode;
	private Map<String, Integer> _handles;
	
	public Shader(String vsc, String fsc) {
		this.vertexShaderCode = vsc;
		this.fragmentShaderCode = fsc;
		_handles = new HashMap<String, Integer>();
	}
	
	public int getUniform(String name) {
		if(_handles.containsKey(name)) {
			return _handles.get(name);
		}
		
		int handle = GLES20.glGetUniformLocation(hProgram, name);
		_handles.put(name, handle);
		return handle;
	}
	
	public int getVertexAttribute(String name) {
		if(_handles.containsKey(name)) {
			return _handles.get(name);
		}
		
		int handle = GLES20.glGetAttribLocation(hProgram, name);
		_handles.put(name, handle);
		return handle;
	}
	
	public int Program() {
		if(hProgram == -1)
			compileProgram();
		return hProgram;
	}

	private void compileProgram() {
		this.hVertexShader = Shader.loadShader(GLES20.GL_VERTEX_SHADER, this.vertexShaderCode);
		this.hFragmentShader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER, this.fragmentShaderCode);
		this.hProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(this.hProgram, this.hVertexShader);
		GLES20.glAttachShader(this.hProgram, this.hFragmentShader);
		GLES20.glLinkProgram(this.hProgram);
	}
	
}
