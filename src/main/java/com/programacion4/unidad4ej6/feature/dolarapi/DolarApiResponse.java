package com.programacion4.unidad4ej6.feature.dolarapi;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DolarApiResponse {

    @JsonProperty("moneda")
    private String moneda;

    @JsonProperty("casa")
    private String casa;

    @JsonProperty("nombre")
    private String nombre;

    @JsonProperty("compra")
    private Double compra;

    @JsonProperty("venta")
    private Double venta;

    @JsonProperty("fechaActualizacion")
    private String fechaActualizacion;

    public DolarApiResponse() {}

    public String getMoneda() { return moneda; }
    public void setMoneda(String moneda) { this.moneda = moneda; }
    public String getCasa() { return casa; }
    public void setCasa(String casa) { this.casa = casa; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Double getCompra() { return compra; }
    public void setCompra(Double compra) { this.compra = compra; }
    public Double getVenta() { return venta; }
    public void setVenta(Double venta) { this.venta = venta; }
    public String getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(String fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
