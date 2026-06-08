package com.buera.alquiler_vehiculos.model.strategies;

public class PrecioPremiun implements PrecioStrategy {
    private double descuento = 0.2; // Descuento del 20% para clientes premium
    @Override
    public double calcularPrecio(double precioBase, Long diasReserva) {
        return precioBase * diasReserva * (1 - descuento); // Aplica un descuento del 20% para clientes premium
    }
    
    @Override
    public String getNombre() {
        return "Precio Premium";
    }

}
