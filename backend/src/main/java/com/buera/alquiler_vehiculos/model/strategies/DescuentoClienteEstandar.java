package com.buera.alquiler_vehiculos.model.strategies;

import org.springframework.stereotype.Component;

@Component
public class DescuentoClienteEstandar implements Descuento {

    @Override
    public double aplicarDescuento(double total) {
        return total;
    }

    @Override
    public String getTipoString() {
        return "ESTANDAR";
    }

}
