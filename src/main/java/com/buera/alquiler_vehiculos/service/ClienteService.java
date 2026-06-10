package com.buera.alquiler_vehiculos.service;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.buera.alquiler_vehiculos.model.Cliente;
import com.buera.alquiler_vehiculos.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    
    // Constructor
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    // Create
    // Guardar un nuevo cliente
    public Cliente guardarCliente(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Ya existe un cliente con ese email"
            );
        }
        return clienteRepository.save(cliente);
    }

    // Read
    // Obtener todos los clientes
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    // Obtener un cliente por su ID
    public Cliente obtenerClientePorId(Long id) {
        return clienteRepository.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                "Cliente no encontrado"
        ));
    }

    // update
    // Actualizar un cliente existente
    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        Cliente cliente = this.obtenerClientePorId(id);
        cliente.setNombre(clienteActualizado.getNombre());
        cliente.setEmail(clienteActualizado.getEmail());
        cliente.setTipoCliente(clienteActualizado.getTipoCliente());
        return clienteRepository.save(cliente);

    }

    // Delete
    // Eliminar un cliente por su ID
    public void eliminarCliente(Long id) {
        Cliente cliente = obtenerClientePorId(id);
        clienteRepository.delete(cliente);
    }


}
