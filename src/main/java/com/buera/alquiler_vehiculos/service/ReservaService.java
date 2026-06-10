package com.buera.alquiler_vehiculos.service;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.buera.alquiler_vehiculos.dto.ReservaRequest;
import com.buera.alquiler_vehiculos.dto.ReservaResponseDTO;
import com.buera.alquiler_vehiculos.mapper.ReservaMapper;
import com.buera.alquiler_vehiculos.model.Cliente;
import com.buera.alquiler_vehiculos.model.Reserva;
import com.buera.alquiler_vehiculos.model.Vehiculo;
import com.buera.alquiler_vehiculos.repository.ClienteRepository;
import com.buera.alquiler_vehiculos.repository.ReservaRepository;
import com.buera.alquiler_vehiculos.repository.VehiculoRepository;
import com.buera.alquiler_vehiculos.service.validator.FechasValidator;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final VehiculoRepository vehiculoRepository;
    private final ClienteRepository clienteRepository;
    private final FechasValidator reservaValidator;
    private final PrecioReservaService precioService;

    public ReservaService(ReservaRepository reservaRepository, 
                        VehiculoRepository vehiculoRepository, 
                        ClienteRepository clienteRepository, 
                        FechasValidator reservaValidator, 
                        PrecioReservaService precioService) {
        this.reservaRepository = reservaRepository;
        this.vehiculoRepository = vehiculoRepository;
        this.clienteRepository = clienteRepository;
        this.reservaValidator = reservaValidator;
        this.precioService = precioService;
    }

    //crear una reserva
    public ReservaResponseDTO creaReserva(ReservaRequest reservaRequest) {
        
        LocalDate fechaInicio = reservaRequest.getFechaInicio();
        LocalDate fechaFin = reservaRequest.getFechaFin();
        reservaValidator.validarFechas(fechaInicio, fechaFin);

        Cliente cliente = clienteRepository.findById(reservaRequest.getClienteId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Cliente no encontrado"
                ));

        Vehiculo vehiculo = vehiculoRepository.findById(reservaRequest.getVehiculoId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vehículo no encontrado"
                ));

        boolean ocupado = reservaRepository
                .existsByVehiculoIdAndFechaInicioLessThanAndFechaFinGreaterThan(
                        vehiculo.getId(),
                        fechaFin,
                        fechaInicio
                );

        if (ocupado) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "El vehículo no está disponible en esas fechas"
            );
        }
        boolean isTelepaseIlimitado = reservaRequest.isTelepaseIlimitado();
        double precioBase = precioService.calcularPrecioBase(
                fechaInicio,
                fechaFin,
                vehiculo.getPrecioDiario()
        );

        double cargoTelepase = precioService.calcularCargoTelepase(isTelepaseIlimitado);

        double precioTotalFinal = precioService.calcularPrecioFinal(
                cliente.getTipoCliente(),
                precioBase,
                cargoTelepase
        );

        
        Reserva reserva = new Reserva();
        reserva.setCliente(cliente);
        reserva.setVehiculo(vehiculo);
        reserva.setFechaInicio(fechaInicio);
        reserva.setFechaFin(fechaFin);
        reserva.setTelepaseIlimitado(isTelepaseIlimitado);
        reserva.setPrecioBase(precioBase);
        reserva.setCargoAdicionalTelepase(cargoTelepase);
        reserva.setPrecioTotal(precioTotalFinal);
        return ReservaMapper.toDTO(reservaRepository.save(reserva));
    }

    //obtener una reserva por su ID
    public ReservaResponseDTO obtenerReservaPorId(Long id) {
        Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Reserva no encontrada"
        ));
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
        Reserva reserva = reservaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Reserva no encontrada"
        ));
        reservaRepository.delete(reserva);
    }


}
