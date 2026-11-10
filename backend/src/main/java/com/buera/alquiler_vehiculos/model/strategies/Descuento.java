package com.buera.alquiler_vehiculos.model.strategies;

public interface Descuento {
    String getTipoString();
    double aplicarDescuento(double total);
}
