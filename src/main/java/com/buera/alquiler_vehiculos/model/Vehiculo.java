package com.buera.alquiler_vehiculos.model;
import com.buera.alquiler_vehiculos.model.enums.CategoriaVehiculo;
import jakarta.persistence.*;

@Entity
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String marca;
    private String modelo;
    private String anio;
    private String dominio;
    private int capacidad;

    @Enumerated(EnumType.STRING)
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