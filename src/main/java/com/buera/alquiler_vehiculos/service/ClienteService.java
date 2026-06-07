package com.buera.alquiler_vehiculos.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.buera.alquiler_vehiculos.model.Cliente;
import com.buera.alquiler_vehiculos.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    
    // Constructor
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Guardar un nuevo cliente
    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // Obtener un cliente por su ID
    public Cliente obtenerClientePorId(Long id) {
        return clienteRepository.findById(id).orElse(null);
    }

    // Eliminar un cliente por su ID
    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    // Obtener todos los clientes
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    // Actualizar un cliente existente
    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        return clienteRepository.findById(id)
                .map(cliente -> {
                    cliente.setNombre(clienteActualizado.getNombre());
                    cliente.setEmail(clienteActualizado.getEmail());
                    cliente.setTipoCliente(clienteActualizado.getTipoCliente());
                    return clienteRepository.save(cliente);
                })
                .orElse(null);
    }


}
