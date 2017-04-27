package io.discloader.discloader.entity;

import java.net.MalformedURLException;
import java.net.URL;

public interface IIcon {

	String toString();

	URL toURL() throws MalformedURLException;

	String getHash();
}
