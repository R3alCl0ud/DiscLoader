package io.discloader.discloader.common.discovery;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import io.discloader.discloader.common.event.IEventListener;

/**
 * All class annotated with this annotation will be loaded into DiscLoader as a mod.
 * @author Perry Berman
 * @since 0.0.1_Alpha
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod {
	/**
	 * @return The Mod's modid
	 */
	String modid() default "";
	
	/**
	 * @return The Mod's name
	 */
	String name() default "";

	/**
	 * @return The Mod's version
	 */
	String version() default "";

	/**
	 * @return The Mod's description
	 */
	String desc() default "";
	
	/**
	 * @return The Mod's author(s).
	 */
	String author() default "";
	
    /**
     * Contains an instance of the mod
     * 
     * @author Perry Berman
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
	public @interface Instance {
		String value() default "";
	}
    
    /**
     * All functions in the main Mod class file will be used as EventHandlers. To listen for event outside of the main file,
     * use an {@link IEventListener} instead.
     * @author perryberman
     * @since 0.0.1
     */
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface EventHandler {

    }

}