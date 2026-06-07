package com.buera.alquiler_vehiculos.service;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.stereotype.Service;

import com.buera.alquiler_vehiculos.model.Cliente;
import com.buera.alquiler_vehiculos.model.Reserva;
import com.buera.alquiler_vehiculos.model.Vehiculo;
import com.buera.alquiler_vehiculos.repository.ClienteRepository;
import com.buera.alquiler_vehiculos.repository.ReservaRepository;
import com.buera.alquiler_vehiculos.repository.VehiculoRepository;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;

    public ReservaService(ReservaRepository reservaRepository, VehiculoRepository vehiculoRepository, ClienteRepository clienteRepository) {
        this.reservaRepository = reservaRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.clienteRepository = clienteRepository;
    }

    //crear una reserva
    public Reserva creaReserva(Long clienteId, Long vehiculoId, LocalDate fechaInicio, LocalDate fechaFin) {
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

        long cantidadDias = ChronoUnit.DAYS.between(fechaInicio, fechaFin);

        double precioTotal = cantidadDias * vehiculo.getPrecioDiario();

        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setVehiculo(vehiculo);
        reserva.setFechaInicio(fechaInicio);
        reserva.setFechaFin(fechaFin);
        reserva.setPrecioTotal(precioTotal);

        return reservaRepository.save(reserva);
    }

    //obtener una reserva por su ID
    public Reserva obtenerReservaPorId(Long id) {
        return reservaRepository.findById(id).orElse(null);
    }
    //obtener todas las reservas
    public List<Reserva> obtenerTodasLasReservas() {
        return reservaRepository.findAll();
    }
    //eliminar una reserva por su ID
    public void eliminarReserva(Long id) {
        reservaRepository.deleteById(id);
    }
    //actualizar una reserva
    public Reserva actualizarReserva(Long id, Reserva reservaActualizada) { 
        Reserva reservaExistente = reservaRepository.findById(id).orElse(null);
        if (reservaExistente != null) {
            reservaExistente.setVehiculo(reservaActualizada.getVehiculo());
            reservaExistente.setCliente(reservaActualizada.getCliente());
            reservaExistente.setFechaInicio(reservaActualizada.getFechaInicio());
            reservaExistente.setFechaFin(reservaActualizada.getFechaFin());
            return reservaRepository.save(reservaExistente);
        }
        return null;
    }

}
