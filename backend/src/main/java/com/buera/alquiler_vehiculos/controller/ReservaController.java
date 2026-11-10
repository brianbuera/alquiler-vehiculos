package com.buera.alquiler_vehiculos.controller;
import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.buera.alquiler_vehiculos.dto.ReservaRequest;
import com.buera.alquiler_vehiculos.dto.ReservaResponseDTO;
import com.buera.alquiler_vehiculos.service.ReservaService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/reservas")
public class ReservaController {
    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }


    //obtener todas las reservas
    @GetMapping
    public List<ReservaResponseDTO> obtenerTodasLasReservas() {
        return reservaService.obtenerTodasLasReservas();
    }


    //crear una reserva
    @PostMapping
    public ReservaResponseDTO crearReserva(@Valid @RequestBody ReservaRequest reservaRequest) {
        return reservaService.creaReserva(reservaRequest);
    }

    //obtener una reserva por su ID
    @GetMapping("/{id}")
    public ReservaResponseDTO obtenerReservaPorId(@PathVariable Long id) {
        return reservaService.obtenerReservaPorId(id);
    }

    //eliminar una reserva por su ID
    @DeleteMapping("/{id}")
    public void eliminarReserva(@PathVariable Long id) {
        reservaService.eliminarReserva(id);
    }
    








}
