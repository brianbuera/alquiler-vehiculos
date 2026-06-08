package com.buera.alquiler_vehiculos.service.validators;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class ReservaValidator {

    public void validarFechas(LocalDate fechaInicio, LocalDate fechaFin) {

        if (fechaInicio == null || fechaFin == null) {
            throw new RuntimeException("Las fechas son obligatorias");
        }

        if (fechaFin.isBefore(fechaInicio) || fechaFin.isEqual(fechaInicio)) {
            throw new RuntimeException("La fecha de fin debe ser posterior a la fecha de inicio");
        }

        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new RuntimeException("La fecha de inicio no puede ser anterior a hoy");
        }
    }
}
