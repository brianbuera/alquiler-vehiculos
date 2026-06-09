package com.buera.alquiler_vehiculos.model.strategies;

public class DescuentoClienteVip implements Descuento {
    private double descuento = 0.4; // Descuento del 40% para clientes VIP
    @Override
    public double aplicarDescuento(double precioBase) {
        return precioBase * (1 - descuento); // Aplica un descuento del 40% para clientes VIP
    }

    @Override
    public String getNombre() {
        return "Precio VIP";
    }
}
