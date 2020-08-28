package com.liferay.sca;

public class DependencyFinder {

	public static void main(String[] args) throws Exception {
		if (args.length == 0) {
			System.out.println("Hello World");
		}
		else {
			System.out.println("Hello " + args[0]);
		}
	}

}