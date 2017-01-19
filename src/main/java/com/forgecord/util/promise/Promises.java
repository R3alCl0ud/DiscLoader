package main.java.com.forgecord.util.promise;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Promises {

	public Object resolveMethod;
	public Object rejectMethod;
	
	
	public Promises(Executor executor) {
		executor.execute(resolveMethod, rejectMethod);
	}
	
	public void resolve(Object value) {
		try {
			((Method) this.resolveMethod).invoke(value);
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
	}
	
	public void reject(Object value)  {
		try {
			((Method) this.rejectMethod).invoke(value);
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
	}

	public Promises then(Object function) {
		this.resolveMethod = function;
		return this;
	}
	
	public Promises error(Object function) {
		this.rejectMethod = function;
		return this;
	}
}

