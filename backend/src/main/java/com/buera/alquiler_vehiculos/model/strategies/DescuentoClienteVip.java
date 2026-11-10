package com.buera.alquiler_vehiculos.model.strategies;
import org.springframework.stereotype.Component;

@Component
public class DescuentoClienteVip implements Descuento {
    private double descuento = 0.4; // Descuento del 40% para clientes VIP
    @Override
    public double aplicarDescuento(double total) {
        return total * (1 - descuento); // Aplica un descuento del 40% para clientes VIP
    }

    @Override
    public String getTipoString() {
        return "VIP";
    }
}
