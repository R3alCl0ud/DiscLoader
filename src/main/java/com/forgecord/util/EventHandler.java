package main.java.com.forgecord.util;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import org.atteo.classindex.ClassIndex;

@Documented
@Target(ElementType.METHOD)
public @interface EventHandler {

}
