package io.discloader.discloader.entity;

import java.net.MalformedURLException;
import java.net.URL;

public interface IGame {

	String getName();

	String getURL();

	URL toURL() throws MalformedURLException;

	boolean isStream();

	boolean equals(Object obj);

}
