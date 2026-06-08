package com.buera.alquiler_vehiculos.mapper;

import com.buera.alquiler_vehiculos.dto.ReservaResponseDTO;
import com.buera.alquiler_vehiculos.model.Reserva;

import java.time.temporal.ChronoUnit;

public class ReservaMapper {

    public static ReservaResponseDTO toDTO(Reserva reserva) {

        long dias = ChronoUnit.DAYS.between(
                reserva.getFechaInicio(),
                reserva.getFechaFin()
        );

        double precioBase = dias * reserva.getVehiculo().getPrecioDiario();
        double precioConDescuento = reserva.getPrecioTotal();
        double descuentoAplicado = precioBase - precioConDescuento;

        ReservaResponseDTO dto = new ReservaResponseDTO();

        dto.setReservaId(reserva.getId());
        dto.setClienteNombre(reserva.getCliente().getNombre());

        dto.setVehiculo(
                reserva.getVehiculo().getMarca() + " " +
                reserva.getVehiculo().getModelo()
        );

        dto.setFechaInicio(reserva.getFechaInicio());
        dto.setFechaFin(reserva.getFechaFin());

        dto.setPrecioBase(precioBase);
        dto.setPorcentajeDescuento(descuentoAplicado / precioBase * 100);
        dto.setDescuentoAplicado(descuentoAplicado);
        dto.setPrecioTotal(precioConDescuento);

        return dto;
    }
}
