package com.example.savingsamuel;

import android.content.Context;
import android.opengl.GLES20;

public class ShaderProgram {
	private static Context _context;
	private static ShaderProgram _uniformColor, _varyingColor, _textured, _shadow;
	
	public static void Init(Context context) {
		_context = context;
		_uniformColor = new ShaderProgram(
				_context.getString(R.string.vertexShaderAttributeP),
				_context.getString(R.string.fragmentShaderUniformC));
		_varyingColor = new ShaderProgram(
				_context.getString(R.string.vertexShaderAttributePC),
				_context.getString(R.string.fragmentShaderVaryingC));
		_textured = new ShaderProgram(
				_context.getString(R.string.vertexShaderTextured),
				_context.getString(R.string.fragmentShaderTextured));
		_shadow = new ShaderProgram(
				_context.getString(R.string.vertexShaderTextured),
				_context.getString(R.string.fragmentShaderShadow));
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
	public static int UniformColor() {
		if(_uniformColor.hProgram == -1) {
			_uniformColor.compileProgram();
		}
		return _uniformColor.hProgram;
	}
	public static int VaryingColor() {
		if(_varyingColor.hProgram == -1) {
			_varyingColor.compileProgram();
		}
		return _varyingColor.hProgram;
	}
	public static int Textured() {
		if(_textured.hProgram == -1) {
			_textured.compileProgram();
		}
		return _textured.hProgram;
	}
	public static int Shadow() {
		if(_shadow.hProgram == -1) {
			_shadow.compileProgram();
		}
		return _shadow.hProgram;
	}
	
	private int hProgram = -1, hVertexShader = -1, hFragmentShader = -1;
	private String vertexShaderCode, fragmentShaderCode;
	
	public ShaderProgram(String vsc, String fsc) {
		this.vertexShaderCode = vsc;
		this.fragmentShaderCode = fsc;
	}
	private void compileProgram() {
		this.hVertexShader = ShaderProgram.loadShader(GLES20.GL_VERTEX_SHADER, this.vertexShaderCode);
		this.hFragmentShader = ShaderProgram.loadShader(GLES20.GL_FRAGMENT_SHADER, this.fragmentShaderCode);
		this.hProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(this.hProgram, this.hVertexShader);
		GLES20.glAttachShader(this.hProgram, this.hFragmentShader);
		GLES20.glLinkProgram(this.hProgram);
	}
	
}
