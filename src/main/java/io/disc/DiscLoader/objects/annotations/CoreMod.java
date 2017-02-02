package io.disc.DiscLoader.objects.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CoreMod {
	// If this boolean is set, then enable coremod flag
	boolean isCore();
}
