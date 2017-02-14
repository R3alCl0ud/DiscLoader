package io.disc.discloader.objects.annotations;

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
	String modid() default "";
	
	String name() default "";

	String version() default "";

	String desc() default "";
	
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
	public @interface Instance {
		String value() default "";
	}
}