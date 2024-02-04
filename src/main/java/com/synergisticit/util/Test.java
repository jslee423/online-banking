package com.synergisticit.util;

import java.util.Base64;

public class Test {

	public static void main(String[] args) {
		String usernamePassword = Base64.getEncoder().encodeToString("jason:123123".getBytes());
		String password = Base64.getEncoder().encodeToString("123123".getBytes());
		System.out.println(usernamePassword);
		System.out.println(password);

	}

}
