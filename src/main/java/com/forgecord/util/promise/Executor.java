package main.java.com.forgecord.util.promise;

import java.lang.reflect.Method;

@FunctionalInterface
public interface Executor {

	void execute(Object... objects);
}