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
import com.buera.alquiler_vehiculos.service.validators.FechasValidator;



@Service
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final ReservaRepository reservaRepository;
    private final FechasValidator fechasValidator;


    public VehiculoService (
        VehiculoRepository vehiculoRepository,
        ReservaRepository reservaRepository,
        FechasValidator fechasValidator){
        this.vehiculoRepository = vehiculoRepository;
        this.reservaRepository = reservaRepository;
        this.fechasValidator = fechasValidator;
    }
    

    // Guardar una lista de vehículos
    public List<Vehiculo> guardarVehiculos(List<Vehiculo> vehiculos){
        return vehiculoRepository.saveAll(vehiculos);
    }

    // Guardar un nuevo vehículo
    public Vehiculo guardarVehiculo(Vehiculo vehiculo){
        boolean existe = vehiculoRepository.existsByDominioIgnoreCase(vehiculo.getDominio());
        if (existe) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Ya existe vehiculo con dominio: " + vehiculo.getDominio());
        }
        return vehiculoRepository.save(vehiculo);
    }

    // Obtener todos los vehículos
    public List<Vehiculo> obtenerVehiculos(){
        return vehiculoRepository.findAll();
    }

    // Obtener un vehículo por su ID
    public Vehiculo obtenerVehiculoPorId(Long id){
        return vehiculoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Vehículo no encontrado"
        ));
    }

    // Obtener solo los vehículos disponibles
    public List<Vehiculo> obtenerVehiculosDisponibles(){
        return vehiculoRepository.findByDisponibleTrue();
    }

    // Eliminar un vehiculo por su ID
    public void eliminarVehiculo(Long id){
        Vehiculo vehiculo = obtenerVehiculoPorId(id);
        if(reservaRepository.existsByVehiculoId(id)){
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "El vehiculo tiene reservas asociadas");
        }
        vehiculoRepository.delete(vehiculo);
    }
    
    //Actualizar un vehiculo por su id
    public Vehiculo actualizarVehiculo(Long id, Vehiculo vehiculoActualizado) {
        Vehiculo vehiculo = obtenerVehiculoPorId(id);
        vehiculo.setMarca(vehiculoActualizado.getMarca());
        vehiculo.setModelo(vehiculoActualizado.getModelo());
        vehiculo.setAnio(vehiculoActualizado.getAnio());
        vehiculo.setDisponible(vehiculoActualizado.isDisponible());
        return vehiculoRepository.save(vehiculo);

    }

    public List<Vehiculo> buscarDisponibles(LocalDate fechaInicio, LocalDate fechaFin) {
        fechasValidator.validarFechas(fechaInicio, fechaFin);
        List<Reserva> reservasCruzadas = reservaRepository.findByFechaInicioBeforeAndFechaFinAfter(fechaFin, fechaInicio);

        List<Long> idsOcupados = reservasCruzadas.stream()
                .map(reserva -> reserva.getVehiculo().getId())
                .toList();

        return vehiculoRepository.findAll().stream()
                .filter(vehiculo -> !idsOcupados.contains(vehiculo.getId()))
                .toList();
    }



}
