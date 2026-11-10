package com.buera.alquiler_vehiculos.model;
import com.buera.alquiler_vehiculos.model.enums.CategoriaVehiculo;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String marca;
    @NotBlank
    private String modelo;
    @NotBlank
    private String anio;
    @NotBlank
    private String dominio;
    @Min(4)
    @Max(8)
    private int capacidad;
    @Enumerated(EnumType.STRING)
    @NotNull
    private CategoriaVehiculo categoria;
    private boolean disponible;
    private double precioDiario;    


    public Vehiculo() {
    }

    public Vehiculo(Long id, String marca, String modelo,
                     String dominio, int capacidad,
                     CategoriaVehiculo categoria,
                     boolean disponible, double precioDiario) {

        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.dominio = dominio;
        this.capacidad = capacidad;
        this.categoria = categoria;
        this.disponible = disponible;
        this.precioDiario = precioDiario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDominio() {
        return dominio;
    }

    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public CategoriaVehiculo getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaVehiculo categoria) {
        this.categoria = categoria;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public double getPrecioDiario() {
        return precioDiario;
    }

    public void setPrecioDiario(double precioDiario) {
        this.precioDiario = precioDiario;
    }
}