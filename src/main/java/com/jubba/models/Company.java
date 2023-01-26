package com.jubba.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Entity
@Table(name = "companys")
public class Company {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @NotBlank
  @Lob
  private String json;

  public Company() {
  }

  public Company(String json) {
    this.json = json;
  }


  public String getJson() {
    return json;
  }

  public void setJson(String json) {
    this.json = json;
  }

}
