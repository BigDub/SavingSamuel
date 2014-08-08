package com.example.savingsamuel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

public class Timer {
	private static Vector<Timer>
		_newTimers = new Vector<Timer>(1),
		_activeTimers = new Vector<Timer>(),
		_expiredTimers = new Vector<Timer>(1);
	
	private static void _register(Timer t) {
		_newTimers.add(t);
	}
	private static void _unregister(Timer t) {
		_expiredTimers.add(t);
	}
	
	public static void Update(float elapsed) {
		if(!_newTimers.isEmpty()) {
			for(Timer t : _newTimers) {
				_activeTimers.add(t);
			}
			_newTimers.clear();
		}
		
		for(Timer t : _activeTimers) {
			t._update(elapsed);
		}
		
		if(!_expiredTimers.isEmpty()) {
			for(Timer t : _expiredTimers) {
				_activeTimers.remove(t);
			}
			_expiredTimers.clear();
		}
	}
	public static void Drop() {
		_activeTimers.clear();
		_expiredTimers.clear();
	}
	
	private Object _caller;
	private float _trigger, _timer;
	private Method _action;
	private boolean _looping;
	
	public Timer(Object caller, float trigger, String methodName) {
		_caller = caller;
		try {
			_action = _caller.getClass().getMethod(methodName, (Class<?>[])null);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		_trigger = trigger;
		_timer = _trigger;
		_looping = false;
		_register(this);
	}
	
	public Timer(Object caller, float trigger, String methodName, float offset) {
		_caller = caller;
		try {
			_action = _caller.getClass().getMethod(methodName, (Class<?>[])null);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		_trigger = trigger;
		_timer = _trigger + offset;
		_looping = false;
		_register(this);
	}
	
	public Timer(Object caller, float trigger, String methodName, float offset, boolean loop) {
		_caller = caller;
		try {
			_action = _caller.getClass().getMethod(methodName, (Class<?>[])null);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		_trigger = trigger;
		_timer = _trigger + offset;
		_looping = loop;
		_register(this);
	}
	
	private void _update(float elapsed) {
		_timer -= elapsed;
		if(_timer > 0) 
			return;
		try {
			_action.invoke(_caller);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(_looping) {
			_timer += _trigger;
		} else {
			_unregister(this);
		}
	}
}
