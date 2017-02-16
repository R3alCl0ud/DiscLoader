package io.discloader.discloader.common.discovery;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CoreMod {
	// If this boolean is set, then enable coremod flag
	boolean isCore();
}
