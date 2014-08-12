package com.example.savingsamuel;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.opengl.GLES20;

public class Shader {
	private static Context cContext;
	private static Shader 
		sUniformColor,
		sVaryingColor,
		sTextured,
		sShadow,
		sTinted,
		sNumbers;

	
	public static void Init(Context context) {
		cContext = context;
		sUniformColor = new Shader(
				R.string.vertexShaderAttributeP,
				R.string.fragmentShaderUniformC);
		sVaryingColor = new Shader(
				R.string.vertexShaderAttributePC,
				R.string.fragmentShaderVaryingC);
		sTextured = new Shader(
				R.string.vertexShaderTextured,
				R.string.fragmentShaderTextured);
		sShadow = new Shader(
				R.string.vertexShaderTextured,
				R.string.fragmentShaderShadow);
		sTinted = new Shader(
				R.string.vertexShaderTextured,
				R.string.fragmentShaderTinted);
		sNumbers = new Shader(
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
	public static Shader UniformColor() { return sUniformColor;	}
	public static Shader VaryingColor() { return sVaryingColor;	}
	public static Shader Textured() { return sTextured; }
	public static Shader Shadow() { return sShadow;	}
	public static Shader TintedTexture() { return sTinted; }
	public static Shader Numbers() { return sNumbers; }
	
	private int hProgram = 0, hVertexShader = 0, hFragmentShader = 0;
	private String vertexShaderCode, fragmentShaderCode;
	private Map<String, Integer> mHandles;
	
	public Shader(int vsc, int fsc) {
		this.vertexShaderCode = cContext.getString(vsc);
		this.fragmentShaderCode = cContext.getString(fsc);
		mHandles = new HashMap<String, Integer>();
	}
	
	public int getUniform(String name) {
		if(mHandles.containsKey(name)) {
			return mHandles.get(name);
		}
		
		int handle = GLES20.glGetUniformLocation(Program(), name);
		mHandles.put(name, handle);
		return handle;
	}
	
	public int getVertexAttribute(String name) {
		if(mHandles.containsKey(name)) {
			return mHandles.get(name);
		}
		
		int handle = GLES20.glGetAttribLocation(Program(), name);
		mHandles.put(name, handle);
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
