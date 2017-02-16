package io.discloader.discloader.common.discovery;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * All class annotated with this annotation will be loaded into DiscLoader as a mod.
 * @author Perry Berman
 * @since 0.0.1_Alpha
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod {
	/**
	 * @return The mod's modid
	 */
	String modid() default "";
	
	/**
	 * @return The mod's name
	 */
	String name() default "";

	/**
	 * @return The mod's version
	 */
	String version() default "";

	/**
	 * @return The mod's description
	 */
	String desc() default "";
	
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
	public @interface Instance {
		String value() default "";
	}
    
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface EventHandler {

    }

}