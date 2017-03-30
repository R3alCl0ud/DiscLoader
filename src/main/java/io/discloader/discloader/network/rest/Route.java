package io.discloader.discloader.network.rest;

import java.util.ArrayList;
import java.util.List;

public class Route {

	private List<Request<?>> queue;
	
	public Route() {
		queue = new ArrayList<>();
	}

}
