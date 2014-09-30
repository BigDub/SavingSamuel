package william.wyatt.savingsamuel;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Vector;

public class Timer {
	private static Vector<Timer>
		vNewTimers = new Vector<Timer>(1),
		vActiveTimers = new Vector<Timer>(),
		vExpiredTimers = new Vector<Timer>(1);
	
	private static void register(Timer t) {
		vNewTimers.add(t);
	}
	private static void unregister(Timer t) {
		vExpiredTimers.add(t);
	}
	
	public static void Update(float elapsed) {
		if(!vNewTimers.isEmpty()) {
			for(Timer t : vNewTimers) {
				vActiveTimers.add(t);
			}
			vNewTimers.clear();
		}
		
		for(Timer t : vActiveTimers) {
			t.update(elapsed);
		}
		
		if(!vExpiredTimers.isEmpty()) {
			for(Timer t : vExpiredTimers) {
				vActiveTimers.remove(t);
			}
			vExpiredTimers.clear();
		}
	}
	public static void Drop() {
		vActiveTimers.clear();
		vExpiredTimers.clear();
	}
	
	private Object oCaller;
	private float fTrigger, fTimer;
	private Method mAction;
	private boolean bLooping;
	
	public Timer(Object caller, float trigger, String methodName) {
		this(caller, trigger, methodName, 0, false);
	}
	
	public Timer(Object caller, float trigger, String methodName, float offset) {
		this(caller, trigger, methodName, offset, false);
	}
	
	public Timer(Object caller, float trigger, String methodName, float offset, boolean loop) {
		oCaller = caller;
		try {
			mAction = oCaller.getClass().getMethod(methodName, (Class<?>[])null);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		fTrigger = trigger;
		fTimer = fTrigger + offset;
		bLooping = loop;
		register(this);
	}
	
	private void update(float elapsed) {
		fTimer -= elapsed;
		if(fTimer > 0) 
			return;
		try {
			mAction.invoke(oCaller);
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
		if(bLooping) {
			fTimer += fTrigger;
		} else {
			unregister(this);
		}
	}
}
