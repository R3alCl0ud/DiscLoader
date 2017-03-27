package io.discloader.discloader.common.exceptions;

import io.discloader.discloader.network.json.ExceptionJSON;

public class AccountTypeException extends Exception {

	private static final long serialVersionUID = -834830576849056357L;

	public AccountTypeException(ExceptionJSON data) {
		super(data.message);
	}

}
