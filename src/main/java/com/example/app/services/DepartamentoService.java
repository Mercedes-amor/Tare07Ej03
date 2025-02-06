package com.example.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.domain.Departamento;
import com.example.app.repositories.DepartamentoRepository;

@Service
public class DepartamentoService {

    @Autowired
    DepartamentoRepository DepartamentoRepository;

    public List<Departamento> obtenerTodos() {

        // Utilizamos el método del repositorio llamado findAll
        // Podríamos usar un sort para ordenar las entradas por un campo
        return DepartamentoRepository.findAll();
    }

    public Departamento obtenerPorId(Long id) {
        // findById() devuelve un Opctional (una clase que envuelve a
        // otra clase para evitar nulos) con lo que hay que ajustarlo

        // Lo más correcto, devolver una excepción
        return DepartamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado"));
    }

    public Departamento add(Departamento Departamento) {

        return DepartamentoRepository.save(Departamento); // .save ya nos devuelve un Departamento
    }

    public Departamento actualizar(Departamento Departamento) {

        return DepartamentoRepository.save(Departamento);
    }

    public void eliminarPorId(Long id) {
        // Ya tiene un método propio para borrar por id
        DepartamentoRepository.deleteById(id);
    }

    public void eliminar(Departamento Departamento) {
        DepartamentoRepository.delete(Departamento);
    }

}
