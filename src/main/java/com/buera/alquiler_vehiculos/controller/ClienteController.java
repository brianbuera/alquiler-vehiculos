package com.buera.alquiler_vehiculos.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.buera.alquiler_vehiculos.model.Cliente;
import com.buera.alquiler_vehiculos.service.ClienteService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    // create
    @PostMapping
    public Cliente crearCliente(@Valid @RequestBody Cliente cliente) {
        return clienteService.guardarCliente(cliente);
    }
    //search
    @GetMapping("/{id}")
    public Cliente obtenerClientePorId(@PathVariable Long id) {
        return clienteService.obtenerClientePorId(id);
    }
    //delete cliente
    @DeleteMapping("/{id}")
    public void eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
    }
    //get clientes all 
    @GetMapping
    public List<Cliente> obtenerTodosLosClientes() {
        return clienteService.obtenerTodosLosClientes();
    }
    //actualizar cliente
    @PutMapping("/{id}")
    public Cliente actualizarCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        return clienteService.actualizarCliente(id, cliente);
    }





}
