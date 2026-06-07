package com.buera.alquiler_vehiculos.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.buera.alquiler_vehiculos.model.Vehiculo;
import com.buera.alquiler_vehiculos.service.VehiculoService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/vehiculos")

public class VehiculoController {
    private final VehiculoService vehiculoService;

    public VehiculoController(VehiculoService vehiculoService){
        this.vehiculoService = vehiculoService;
    }

    @GetMapping
    public List<Vehiculo> listarVehiculos(){
        return vehiculoService.obtenerVehiculos();
    }
    
    @GetMapping("/{id}")
    public Vehiculo obtenerVehiculoPorId(@PathVariable Long id){
        return vehiculoService.obtenerVehiculoPorId(id);
    }

    @GetMapping("/disponibles/all")
    public List<Vehiculo> listarVehiculosDisponiblesAllList(){
        return vehiculoService.obtenerVehiculosDisponibles();
    }

    @GetMapping("/disponibles")
    public List<Vehiculo> buscarDisponibles(
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fin
    ) {
        return vehiculoService.buscarDisponibles(inicio, fin);
    }

    @PostMapping
    public Vehiculo crearVehiculo(@RequestBody Vehiculo vehiculo){
        return vehiculoService.guardarVehiculo(vehiculo);
    }

    @PostMapping("/lista")
    public List<Vehiculo> crearVehiculos(@RequestBody List<Vehiculo> vehiculos){
        return vehiculoService.guardarVehiculos(vehiculos);
    }
    
    @DeleteMapping("/{id}")
    public void eliminarVehiculo(@PathVariable Long id){
        vehiculoService.eliminarVehiculo(id);
    }

    @PutMapping("/{id}")
    public Vehiculo actualizarVehiculo(@PathVariable Long id, @RequestBody Vehiculo vehiculo){
        return vehiculoService.actualizarVehiculo(id, vehiculo);
    }
    
}