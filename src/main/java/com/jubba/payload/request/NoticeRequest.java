package com.jubba.payload.request;

import javax.validation.constraints.NotBlank;

public class NoticeRequest {
	@NotBlank
	public String email;
	@NotBlank
	public String servico;

	@NotBlank
	public String assunto;

}
