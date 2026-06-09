package com.buera.alquiler_vehiculos.model.strategies;

public class DescuentoClientePremium implements Descuento {
    private double descuento = 0.2; // Descuento del 20% para clientes premium
    @Override
    public double aplicarDescuento(double precioBase) {
        return precioBase * (1 - descuento); // Aplica un descuento del 20% para clientes premium
    }
    
    @Override
    public String getNombre() {
        return "Precio Premium";
    }

}
