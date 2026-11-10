package com.buera.alquiler_vehiculos.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.buera.alquiler_vehiculos.model.Vehiculo;
import org.springframework.stereotype.Repository;

@Repository
public interface VehiculoRepository extends JpaRepository <Vehiculo, Long> {
    List<Vehiculo> findByDisponibleTrue();
    boolean existsByDominioIgnoreCase(String dominio);
}
