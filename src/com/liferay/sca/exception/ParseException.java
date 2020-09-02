package com.liferay.sca.exception;

import java.io.File;

public class ParseException extends Exception {

	public ParseException(String location, File file) {
		super("In " + file.getPath() + " near " + location);
	}

	public ParseException(String msg) {
		super(msg);
	}

}