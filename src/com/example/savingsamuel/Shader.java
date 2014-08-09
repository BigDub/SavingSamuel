package com.example.savingsamuel;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.opengl.GLES20;

public class Shader {
	private static Context _context;
	private static Shader 
		_uniformColor,
		_varyingColor,
		_textured,
		_shadow,
		_tinted,
		_numbers;

	
	public static void Init(Context context) {
		_context = context;
		_uniformColor = new Shader(
				R.string.vertexShaderAttributeP,
				R.string.fragmentShaderUniformC);
		_varyingColor = new Shader(
				R.string.vertexShaderAttributePC,
				R.string.fragmentShaderVaryingC);
		_textured = new Shader(
				R.string.vertexShaderTextured,
				R.string.fragmentShaderTextured);
		_shadow = new Shader(
				R.string.vertexShaderTextured,
				R.string.fragmentShaderShadow);
		_tinted = new Shader(
				R.string.vertexShaderTextured,
				R.string.fragmentShaderTinted);
		_numbers = new Shader(
				R.string.vertexShaderNumbers,
				R.string.fragmentShaderTextured);
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
	public static Shader UniformColor() { return _uniformColor;	}
	public static Shader VaryingColor() { return _varyingColor;	}
	public static Shader Textured() { return _textured; }
	public static Shader Shadow() { return _shadow;	}
	public static Shader TintedTexture() { return _tinted; }
	public static Shader Numbers() { return _numbers; }
	
	private int hProgram = 0, hVertexShader = 0, hFragmentShader = 0;
	private String vertexShaderCode, fragmentShaderCode;
	private Map<String, Integer> _handles;
	
	public Shader(int vsc, int fsc) {
		this.vertexShaderCode = _context.getString(vsc);
		this.fragmentShaderCode = _context.getString(fsc);
		_handles = new HashMap<String, Integer>();
	}
	
	public int getUniform(String name) {
		if(_handles.containsKey(name)) {
			return _handles.get(name);
		}
		
		int handle = GLES20.glGetUniformLocation(Program(), name);
		_handles.put(name, handle);
		return handle;
	}
	
	public int getVertexAttribute(String name) {
		if(_handles.containsKey(name)) {
			return _handles.get(name);
		}
		
		int handle = GLES20.glGetAttribLocation(Program(), name);
		_handles.put(name, handle);
		return handle;
	}
	
	public int Program() {
		if(hProgram == 0)
			compileProgram();
		return hProgram;
	}

	private void compileProgram() {
		if(hVertexShader == 0)
			hVertexShader = Shader.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
		if(hFragmentShader == 0)
			hFragmentShader = Shader.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
		hProgram = GLES20.glCreateProgram();
		GLES20.glAttachShader(hProgram, hVertexShader);
		GLES20.glAttachShader(hProgram, hFragmentShader);
		GLES20.glLinkProgram(hProgram);
	}
	
}
