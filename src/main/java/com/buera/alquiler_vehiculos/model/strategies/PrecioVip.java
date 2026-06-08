package com.buera.alquiler_vehiculos.model.strategies;

public class PrecioVip implements PrecioStrategy {
    private double descuento = 0.4; // Descuento del 40% para clientes VIP
    @Override
    public double calcularPrecio(double precioBase, Long diasReserva) {
        return precioBase * diasReserva * (1 - descuento); // Aplica un descuento del 40% para clientes VIP
    }

    @Override
    public String getNombre() {
        return "Precio VIP";
    }
}
