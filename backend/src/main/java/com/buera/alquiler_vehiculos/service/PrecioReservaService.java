package com.buera.alquiler_vehiculos.service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.buera.alquiler_vehiculos.model.enums.TipoCliente;
import com.buera.alquiler_vehiculos.model.strategies.Descuento;


@Service
public class PrecioReservaService {
    private static final double CARGO_TELEPASE = 6000;
    private final Map<String, Descuento> strategies;


    public PrecioReservaService(List<Descuento> strategies) {
        this.strategies = strategies.stream().collect(Collectors.toMap(
                Descuento::getTipoString, // clave -> tipoCliente
                strategy -> strategy
        ));
    }
    //calcular el precio total de una reserva
    public double calcularPrecioFinal(TipoCliente tipoCliente, double total, double cargoAdicionalTelepase) {
        Descuento strategy = strategies.get(tipoCliente.name());
        return strategy.aplicarDescuento(total + cargoAdicionalTelepase);
    }

    public double calcularCargoTelepase(boolean isTelepaseIlimitado) {
        return isTelepaseIlimitado ? CARGO_TELEPASE : 0;
    }

    public double calcularPrecioBase(LocalDate fechaInicio, LocalDate fechaFin, double precioDiario) {
         long dias = java.time.temporal.ChronoUnit.DAYS.between(
                fechaInicio,
                fechaFin
        );
        return dias * precioDiario;
    }


}
