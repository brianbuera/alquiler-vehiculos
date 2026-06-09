package com.buera.alquiler_vehiculos.service;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.buera.alquiler_vehiculos.model.Reserva;
import com.buera.alquiler_vehiculos.model.Vehiculo;
import com.buera.alquiler_vehiculos.repository.ReservaRepository;
import com.buera.alquiler_vehiculos.repository.VehiculoRepository;



@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ReservaRepository reservaRepository;

    public VehiculoService (VehiculoRepository vehiculoRepository, ReservaRepository reservaRepository){
        this.vehiculoRepository = vehiculoRepository;
        this.reservaRepository = reservaRepository;
    }
    // Guardar una lista de vehículos
    public List<Vehiculo> guardarVehiculos(List<Vehiculo> vehiculos){
        return vehiculoRepository.saveAll(vehiculos);
    }
    // Guardar un nuevo vehículo
    public Vehiculo guardarVehiculo(Vehiculo vehiculo){
        return vehiculoRepository.save(vehiculo);
    }

    // Obtener un vehículo por su ID
    public Vehiculo obtenerVehiculoPorId(Long id){
        return vehiculoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Vehículo no encontrado"
        ));
    }

    // Obtener todos los vehículos
    public List<Vehiculo> obtenerVehiculos(){
        return vehiculoRepository.findAll();
    }

    // Obtener solo los vehículos disponibles
    public List<Vehiculo> obtenerVehiculosDisponibles(){
        return vehiculoRepository.findByDisponibleTrue();
    }

    // Eliminar un vehiculo por su ID
    public void eliminarVehiculo(Long id){
        vehiculoRepository.deleteById(id);
    }

    public Vehiculo actualizarVehiculo(Long id, Vehiculo vehiculoActualizado) {
        return vehiculoRepository.findById(id)
                .map(vehiculo -> {
                    vehiculo.setMarca(vehiculoActualizado.getMarca());
                    vehiculo.setModelo(vehiculoActualizado.getModelo());
                    vehiculo.setAnio(vehiculoActualizado.getAnio());
                    vehiculo.setDisponible(vehiculoActualizado.isDisponible());
                    return vehiculoRepository.save(vehiculo);
                })
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Vehículo no encontrado"
                ));
    }

    public List<Vehiculo> buscarDisponibles(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Reserva> reservasCruzadas = reservaRepository.findByFechaInicioBeforeAndFechaFinAfter(fechaFin, fechaInicio);

        List<Long> idsOcupados = reservasCruzadas.stream()
                .map(reserva -> reserva.getVehiculo().getId())
                .toList();

        return vehiculoRepository.findAll().stream()
                .filter(vehiculo -> !idsOcupados.contains(vehiculo.getId()))
                .toList();
    }



}
