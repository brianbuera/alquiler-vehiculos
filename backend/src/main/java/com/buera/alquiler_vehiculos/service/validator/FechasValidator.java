package com.buera.alquiler_vehiculos.service.validator;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class FechasValidator {

    public FechasValidator(){
        
    }

    public void validarFechas(LocalDate fechaInicio, LocalDate fechaFin) {

        if (fechaInicio == null || fechaFin == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Las fechas son obligatorias"
            );
        }

        if (fechaFin.isBefore(fechaInicio) || fechaFin.isEqual(fechaInicio)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de fin debe ser posterior a la fecha de inicio"
            );
        }

        if (fechaInicio.isBefore(LocalDate.now())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "La fecha de inicio no puede ser anterior a hoy"
            );
        }
    }

}
