package com.buera.alquiler_vehiculos.service;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.buera.alquiler_vehiculos.model.Reserva;
import com.buera.alquiler_vehiculos.model.enums.TipoCliente;
import com.buera.alquiler_vehiculos.model.strategies.DescuentoClienteEstandar;
import com.buera.alquiler_vehiculos.model.strategies.DescuentoClientePremium;
import com.buera.alquiler_vehiculos.model.strategies.Descuento;
import com.buera.alquiler_vehiculos.model.strategies.DescuentoClienteVip;

@Service
public class PrecioReservaService {
    private static final double CARGO_TELEPASE = 6000;
    private final Map<TipoCliente, Descuento> strategies = Map.of(
            TipoCliente.ESTANDAR, new DescuentoClienteEstandar(),
            TipoCliente.PREMIUM, new DescuentoClientePremium(),
            TipoCliente.VIP, new DescuentoClienteVip()
    );
    //calcular el precio total de una reserva
    public double calcularPrecioFinal(Reserva reserva) {
        Descuento strategy = strategies.get(reserva.getCliente().getTipoCliente());
        double adicional = reserva.isTelepaseIlimitado() ? CARGO_TELEPASE : 0;
        return strategy.aplicarDescuento(reserva.getPrecioBase() + adicional);
    }

    public double getCargoAdicionalTelepase() {
        return CARGO_TELEPASE;
    }

    public double calcularPrecioBase(Reserva reserva) {
        long dias = java.time.temporal.ChronoUnit.DAYS.between(
                reserva.getFechaInicio(),
                reserva.getFechaFin()
        );
        return dias * reserva.getVehiculo().getPrecioDiario();
    }


}
