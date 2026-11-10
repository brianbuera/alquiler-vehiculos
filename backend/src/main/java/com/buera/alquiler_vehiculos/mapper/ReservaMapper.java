package com.buera.alquiler_vehiculos.mapper;

import com.buera.alquiler_vehiculos.dto.ReservaResponseDTO;
import com.buera.alquiler_vehiculos.model.Reserva;


public class ReservaMapper {

    public static ReservaResponseDTO toDTO(Reserva reserva) {

        double precioBase = reserva.getPrecioBase();
        double descuento =  reserva.getPrecioBase() > reserva.getPrecioTotal() ? (reserva.getPrecioBase() + reserva.getCargoAdicionalTelepase())- reserva.getPrecioTotal() : 0;
        double porcentajeDescuento = (descuento / (precioBase + reserva.getCargoAdicionalTelepase())) * 100;

        ReservaResponseDTO dto = new ReservaResponseDTO();

        dto.setReservaId(reserva.getId());
        dto.setClienteNombre(reserva.getCliente().getNombre());
        dto.setTipoCliente(reserva.getCliente().getTipoCliente().name());

        dto.setVehiculo(
                reserva.getVehiculo().getMarca() + " " +
                reserva.getVehiculo().getModelo()
        );

        dto.setFechaInicio(reserva.getFechaInicio());
        dto.setFechaFin(reserva.getFechaFin());
        dto.setPrecioBase(precioBase);
        dto.setCargoTelepaseIlimitado(reserva.getCargoAdicionalTelepase());
        dto.setPorcentajeDescuento(porcentajeDescuento);
        dto.setPrecioTotal(reserva.getPrecioTotal());

        return dto;
    }
}
