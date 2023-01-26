package com.jubba.payload.request;

import javax.validation.constraints.NotBlank;

public class ForgotPasswordEmailRequest {
	@NotBlank
	private String email;
	@NotBlank
	private String username;

	public String getEmail() {
		return email;
	}

	public void setEmail(String username) {
		this.email = username;
	}
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
