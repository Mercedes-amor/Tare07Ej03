package com.example.app.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.app.domain.Empleado;
import com.example.app.domain.Nomina;
import com.example.app.repositories.NominaRepository;

@Service
public class NominaService {

    @Autowired
    NominaRepository nominaRepository;

    @Autowired
    EmpleadoService empleadoService;

    public List<Nomina> obtenerTodos() {
        return nominaRepository.findAll();
    }

    public void cargarNominasFichero() {
        try {

            List<String> lineas = Files.readAllLines(Paths.get("Ej03/src/main/resources/static/nominas.csv"),
                    StandardCharsets.UTF_8);
            System.out.println("Nóminas cargadas correctamente");

            for (String linea : lineas) {
                try {
                    // Usamos ; como delimitador
                    String[] nominaInfo = linea.split(";");

                    // Procesamos la línea, nos irá devolviendo una instancia de Nommina
                    // que es un array con 5 elementos.
                    // [id_Empleado, Fecha, importeBruto, porcentImpuestos, importeNeto]

                    LocalDate fecha = LocalDate.parse(nominaInfo[1].trim());
                    Double importeBruto = Double.parseDouble(nominaInfo[2].trim());
                    Double porcentImpuestos = Double.parseDouble(nominaInfo[3].trim());
                    Double importeNeto = Double.parseDouble(nominaInfo[4].trim());
                    Long idEmpleado = Long.parseLong(nominaInfo[5].trim());
                    
                    //Obtenemos el empleado
                    Empleado empleado = empleadoService.obtenerPorId(idEmpleado);

                    // Creamos el objeto Nomina
                    Nomina nomina = new Nomina(null, fecha, importeBruto, porcentImpuestos, importeNeto, empleado);

                    // Comprobamos si ya existe una nómina para el mismo empleado y misma fecha
                    if (!comprobarNominaEmpleado(nomina)) {
                        nominaRepository.save(nomina);
                        System.out.println("Nóminas cargadas correctamente del CSV");
                    } else {
                        throw new RuntimeException(
                                "La nómina para el empleado " + empleado.getNombre() + " en la fecha " + fecha + " ya existe.");
                    }

                } catch (Exception e) {
                    System.err.println("Error al leer el archivo nominas.csv: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo csv: " + e.getMessage());
        }
    }

    // Método comprobar si ya hay una nómina de misma fecha y empleado
    public boolean comprobarNominaEmpleado(Nomina nomina) {
        if (nomina == null || nomina.getEmpleado() == null) {
            throw new IllegalArgumentException("La nómina o el empleado no pueden ser nulos.");
        }

        LocalDate fechaAComprobar = nomina.getFechaNomina();
        Long idEmpleadoAComprobar = nomina.getEmpleado().getId();

        return nominaRepository.existsByEmpleadoIdAndFechaNomina(idEmpleadoAComprobar, fechaAComprobar);
    }

    public Nomina obtenerPorId(Long id) {
        // findById() devuelve un Opctional (una clase que envuelve a
        // otra clase para evitar nulos) con lo que hay que ajustarlo

        // Lo más correcto, devolver una excepción
        return nominaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nómina no encontrada"));
    }

    public Nomina add(Nomina nomina) {

        if(comprobarNominaEmpleado(nomina)){
            throw new RuntimeException(
                                "La nómina para el empleado " + nomina.getEmpleado().getNombre() + " en la fecha " + nomina.getFechaNomina() + " ya existe.");
        }
        return nominaRepository.save(nomina);
    }

    public Nomina actualizar(Nomina nomina) {
        return nominaRepository.save(nomina);
    }

    public void eliminarPorId(Long id) {
        Optional<Nomina> nomina = nominaRepository.findById(id);
        if (nomina.isPresent()) {
            System.out.println("Eliminando nómina: " + nomina.get());
            nominaRepository.deleteById(id);
        } else {
            System.out.println("Nómina con ID " + id + " no encontrada");
        }
    }

    public void eliminar(Nomina nomina) {
        nominaRepository.delete(nomina);
    }

}
