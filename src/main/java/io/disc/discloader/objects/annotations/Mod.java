package io.disc.discloader.objects.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Mod {
	String modid();
	
	String name();

	String version();

	String desc();
	
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
	public @interface Instance {
		String value() default "";
	}
}