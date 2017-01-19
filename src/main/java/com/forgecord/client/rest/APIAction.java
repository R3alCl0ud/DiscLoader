package main.java.com.forgecord.client.rest;

import java.util.function.Consumer;

public abstract class APIAction<T> {
	public final Consumer<T> onSuccess;
    public final Consumer<Throwable> onFailure;
	
	public APIAction(APIRequest<T> apiRequest, Consumer<T> onSuccess, Consumer<Throwable> onFailure) {
		this.onSuccess = onSuccess;
		this.onFailure = onFailure;
	}
	

}
