package com.buera.alquiler_vehiculos.model.strategies;
import org.springframework.stereotype.Component;

@Component
public class DescuentoClientePremium implements Descuento {
    private double descuento = 0.2; // Descuento del 20% para clientes premium
    @Override
    public double aplicarDescuento(double total) {
        return total * (1 - descuento); // Aplica un descuento del 20% para clientes premium
    }
    
    @Override
    public String getTipoString() {
        return "PREMIUM";
    }

}
