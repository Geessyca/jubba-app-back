package com.jubba.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "schedule")
public class Schedule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @NotBlank
  @Lob
  private String json;


  private Integer idCliente;

  public Schedule() {
  }

  public Schedule(String json, Integer idCliente) {
    this.json = json;
    this.idCliente = idCliente;
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
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }
}
