package br.com.alura.model;

import java.time.LocalDate;

public class Bitcoin {
  private Long id;
  private Double preço;
  private String tipo;
  private LocalDate data;
  public Long getId() {
    return id;
  }
  public LocalDate getData() {
    return data;
  }
  public void setData(LocalDate data) {
    this.data = data;
  }
  public String getTipo() {
    return tipo;
  }
  public void setTipo(String tipo) {
    this.tipo = tipo;
  }
  public Double getPreço() {
    return preço;
  }
  public void setPreço(Double preço) {
    this.preço = preço;
  }
  public void setId(Long id) {
    this.id = id;
  }

}