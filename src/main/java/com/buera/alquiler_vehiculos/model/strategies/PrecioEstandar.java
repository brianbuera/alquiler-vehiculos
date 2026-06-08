package com.buera.alquiler_vehiculos.model.strategies;

public class PrecioEstandar implements PrecioStrategy {

    @Override
    public double calcularPrecio(double precioBase, Long diasReserva) {
        return precioBase * diasReserva;
    }

    @Override
    public String getNombre() {
        return "Precio Estandar";
    }

}
