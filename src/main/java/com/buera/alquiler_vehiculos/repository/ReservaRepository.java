package com.buera.alquiler_vehiculos.repository;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.buera.alquiler_vehiculos.model.Reserva;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long>{
    
    boolean existsByVehiculoIdAndFechaInicioLessThanAndFechaFinGreaterThan(
        Long vehiculoId,
        LocalDate fechaFin,
        LocalDate fechaInicio
    );

    public List<Reserva> findByFechaInicioBeforeAndFechaFinAfter(LocalDate fechaFin, LocalDate fechaInicio);

}
