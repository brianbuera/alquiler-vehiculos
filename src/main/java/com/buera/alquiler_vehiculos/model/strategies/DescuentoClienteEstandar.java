package com.buera.alquiler_vehiculos.model.strategies;

public class DescuentoClienteEstandar implements Descuento {

    @Override
    public double aplicarDescuento(double precioBase) {
        return precioBase;
    }

    @Override
    public String getNombre() {
        return "Precio Estandar";
    }

}
