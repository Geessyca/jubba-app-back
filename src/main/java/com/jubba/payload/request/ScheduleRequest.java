package com.jubba.payload.request;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

public class ScheduleRequest {

	@NotBlank
	public String json;


	public Integer idCliente;


	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public Integer getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(Integer idCliente) {
		this.idCliente = idCliente;
	}
}
