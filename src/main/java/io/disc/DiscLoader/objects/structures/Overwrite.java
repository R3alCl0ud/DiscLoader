/**
 * 
 */
package io.disc.DiscLoader.objects.structures;

import io.disc.DiscLoader.objects.gateway.OverwriteJSON;

/**
 * @author Perry Berman
 *
 */
public class Overwrite {

	public int allow;
	public int deny;
	
	public Overwrite(OverwriteJSON data) {
		this.allow = data.allow;
		this.deny = data.deny;
	}

}
