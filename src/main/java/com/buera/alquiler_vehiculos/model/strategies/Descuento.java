package com.buera.alquiler_vehiculos.model.strategies;

public interface Descuento {
    String getNombre();
    double aplicarDescuento(double precioBase);
}
