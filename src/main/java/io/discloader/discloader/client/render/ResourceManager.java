package io.discloader.discloader.client.render;

import java.util.Collection;
import java.util.HashMap;

import io.discloader.discloader.client.render.util.Resource;

/**
 * @author Perry Berman
 *
 */
public class ResourceManager {
	
	public static final ResourceManager instance = new ResourceManager();
	
	private HashMap<String, Resource> resources = new HashMap<>();
	
	private ResourceManager() {
		addResource(new Resource("discloader", "texture/commands/missing-texture.png"));
		addResource(new Resource("discloader", "texture/commands/help.png"));
		addResource(new Resource("discloader", "texture/commands/mods.png"));
	}
	
	public void addResource(Resource r) {
		resources.put(r.getName(), r);
	}
	
	public Resource getResource(String name) {
		return resources.get(name);
	}
	
	public Collection<Resource> getResources() {
		return resources.values();
	}
	
}
