package com.buera.alquiler_vehiculos.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import org.springframework.stereotype.Service;

import com.buera.alquiler_vehiculos.model.Cliente;
import com.buera.alquiler_vehiculos.model.Vehiculo;
import com.buera.alquiler_vehiculos.model.enums.TipoCliente;
import com.buera.alquiler_vehiculos.model.strategies.PrecioEstandar;
import com.buera.alquiler_vehiculos.model.strategies.PrecioPremiun;
import com.buera.alquiler_vehiculos.model.strategies.PrecioStrategy;
import com.buera.alquiler_vehiculos.model.strategies.PrecioVip;

@Service
public class PrecioService {
    //calcular el precio total de una reserva
    public double calcularPrecio(Cliente cliente, Vehiculo vehiculo, LocalDate fechaInicio, LocalDate fechaFin) {
        long cantidadDias = ChronoUnit.DAYS.between(fechaInicio, fechaFin);
        PrecioStrategy strategy = obtenerStrategy(cliente.getTipoCliente());
        return strategy.calcularPrecio(vehiculo.getPrecioDiario(), cantidadDias);
    }

    //obtener la estrategia de precio según el tipo de cliente
    private PrecioStrategy obtenerStrategy(TipoCliente tipoCliente) {
        if (tipoCliente == TipoCliente.PREMIUM) {
            return new PrecioPremiun();
        }
        if (tipoCliente == TipoCliente.VIP) {
            return new PrecioVip();
        }
        return new PrecioEstandar();
}
}
