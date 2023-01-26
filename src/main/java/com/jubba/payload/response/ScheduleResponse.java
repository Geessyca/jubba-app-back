package com.jubba.payload.response;

import java.util.List;

public class ScheduleResponse {
	private Long id;
	private String json;
	private Integer idCliente;


	public ScheduleResponse(Long id, String json, Integer idCliente) {
		this.id = id;
		this.json = json;
		this.idCliente = idCliente;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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
