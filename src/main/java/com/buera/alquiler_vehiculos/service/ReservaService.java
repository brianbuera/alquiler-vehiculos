package com.buera.alquiler_vehiculos.service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.buera.alquiler_vehiculos.dto.ReservaResponseDTO;
import com.buera.alquiler_vehiculos.mapper.ReservaMapper;
import com.buera.alquiler_vehiculos.model.Cliente;
import com.buera.alquiler_vehiculos.model.Reserva;
import com.buera.alquiler_vehiculos.model.Vehiculo;
import com.buera.alquiler_vehiculos.repository.ClienteRepository;
import com.buera.alquiler_vehiculos.repository.ReservaRepository;
import com.buera.alquiler_vehiculos.repository.VehiculoRepository;
import com.buera.alquiler_vehiculos.service.validators.ReservaValidator;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;
    private final ReservaValidator reservaValidator;
    private final PrecioService precioService;

    public ReservaService(ReservaRepository reservaRepository, 
                        VehiculoRepository vehiculoRepository, 
                        ClienteRepository clienteRepository, 
                        ReservaValidator reservaValidator, 
                        PrecioService precioService) {
        this.reservaRepository = reservaRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.clienteRepository = clienteRepository;
        this.reservaValidator = reservaValidator;
        this.precioService = precioService;
    }

    //crear una reserva
    public ReservaResponseDTO creaReserva(Long clienteId, Long vehiculoId, LocalDate fechaInicio, LocalDate fechaFin) {
        reservaValidator.validarFechas(fechaInicio, fechaFin);
        
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado"));

        boolean ocupado = reservaRepository
                .existsByVehiculoIdAndFechaInicioLessThanAndFechaFinGreaterThan(
                        vehiculoId,
                        fechaFin,
                        fechaInicio
                );

        if (ocupado) {
            throw new RuntimeException("El vehículo no está disponible en esas fechas");
        }

        double precioTotal = precioService.calcularPrecio(cliente, vehiculo, fechaInicio, fechaFin);

        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setVehiculo(vehiculo);
        reserva.setFechaInicio(fechaInicio);
        reserva.setFechaFin(fechaFin);
        reserva.setPrecioTotal(precioTotal);

        return ReservaMapper.toDTO(reservaRepository.save(reserva));
    }

    //obtener una reserva por su ID
    public ReservaResponseDTO obtenerReservaPorId(Long id) {
        Reserva reserva = reservaRepository.findById(id).orElse(null);
        return ReservaMapper.toDTO(reserva);
    }

    //obtener todas las reservas
    public List<ReservaResponseDTO> obtenerTodasLasReservas() {
        return reservaRepository.findAll().stream()
                .map(ReservaMapper::toDTO)
                .collect(Collectors.toList());
    }

    //eliminar una reserva por su ID
    public void eliminarReserva(Long id) {
        reservaRepository.deleteById(id);
    }

    //actualizar una reserva
    public ReservaResponseDTO actualizarReserva(Long id, Reserva reservaActualizada) { 
        Reserva reservaExistente = reservaRepository.findById(id).orElse(null);
        if (reservaExistente != null) {
            reservaExistente.setVehiculo(reservaActualizada.getVehiculo());
            reservaExistente.setCliente(reservaActualizada.getCliente());
            reservaExistente.setFechaInicio(reservaActualizada.getFechaInicio());
            reservaExistente.setFechaFin(reservaActualizada.getFechaFin());
            return ReservaMapper.toDTO(reservaRepository.save(reservaExistente));
        }
        return null;
    }

 
}
