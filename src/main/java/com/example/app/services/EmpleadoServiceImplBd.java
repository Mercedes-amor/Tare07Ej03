package com.example.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import com.example.app.domain.Departamento;
import com.example.app.domain.Empleado;
import com.example.app.domain.Genero;
import com.example.app.repositories.EmpleadoRepository;

@Service
@Primary
public class EmpleadoServiceImplBd implements EmpleadoService {

    @Autowired
    EmpleadoRepository empleadoRepository;

    public List<Empleado> obtenerTodos() {

        // Utilizamos el método del repositorio llamado findAll
        // Podríamos usar un sort para ordenar las entradas por un campo
        return empleadoRepository.findAll();
    }

    public List<Empleado> obtenerPorSalarioMayor(Float salario) {
        return empleadoRepository.findEmpleadosConSalarioMayorIgual(salario);
    }

    public Empleado obtenerMaxIdEmpleado() {
        return empleadoRepository.obtenerMaxIdEmpleado();
    }

    public Empleado obtenerPorId(Long id) {
        // findById() devuelve un Opctional (una clase que envuelve a
        // otra clase para evitar nulos) con lo que hay que ajustarlo

        // Lo más sencillo .orElse(null) -> Devuelve un null si no lo encuentras
        // empleadoRepository.findById(id).orElse(null);

        // Lo más correcto, devolver una excepción
        return empleadoRepository.findById(id).orElseThrow(() -> new RuntimeException("Empleado no encontrado"));
    }

    public Empleado add(Empleado empleado) {

        // Solamente debemos cambiar el .add por .save

        // Lanzamos excepción si el salario es inferior a 1800
        if (empleado.getSalario() < 18000) {
            throw new RuntimeException("El salario no puede ser inferior a 18.000€");
        }
        // Usamos la función creada obtenerPresupuestoRestante() para obtener el
        // presupuesto restante del departamento.
        Float presupuestoRestante = obtenerPresupuestoRestante(empleado.getDepartamento());

        // Lanzamos excepción si el salario supera el límite de prespuestoAnual del
        // departamento
        if (empleado.getSalario() > presupuestoRestante) {
            throw new RuntimeException("El salario no puede superar el presupuesto anual restante del departamento");
        }
        return empleadoRepository.save(empleado); // .save ya nos devuelve un empleado
    }

    public Empleado actualizar(Empleado empleado) {
        // Comprobación de que el salario introducido no es menor a 18.000€,
        // Si es así lanza una excepción
        if (empleado.getSalario() < 18000) {
            throw new RuntimeException("El salario no puede ser inferior a 18.000€");
        }
        // Usamos la función creada obtenerPresupuestoRestante() para obtener el
        // presupuesto restante del departamento.
        Float presupuestoRestante = obtenerPresupuestoRestante(empleado.getDepartamento());

        // Lanzamos excepción si el salario supera el límite de prespuestoAnual del
        // departamento
        if (empleado.getSalario() > presupuestoRestante) {
            throw new RuntimeException("El salario no puede superar el presupuesto anual restante del departamento");
        }

        return empleadoRepository.save(empleado);
    }

    public void eliminarPorId(Long id) {
        // Ya tiene un método propio para borrar por id
        empleadoRepository.deleteById(id);
    }

    public void eliminar(Empleado empleado) {
        empleadoRepository.delete(empleado);
    }

    // DEPARTAMENTO

    public List<Empleado> obtenerPorDepartamento(Departamento departamento) {
        return empleadoRepository.findByDepartamento(departamento);
    }

    public List<Empleado> obtenerPorDepartamentoId(Long departamentoId) {
        return empleadoRepository.findByDepartamentoId(departamentoId);
    }

    // Método para saber el presupuesto pendiente de asignar de un departamento
    public Float obtenerPresupuestoRestante(Departamento departamento) {
        // Variable para almacenar el presupuesto asigando en salarios del depto
        float presupuestoAsigando = 0;
        // Array de empleados de un departamento
        List<Empleado> empleadosDepto = departamento.getEmpleados();

        for (Empleado empleado : empleadosDepto) {
            // Vamos sumando el salario de cada empleado a la variable local
            presupuestoAsigando += empleado.getSalario();
        }
        return departamento.getPresupuestoAnual() - presupuestoAsigando;
    }

    // BUSCADOR
    public List<Empleado> buscarPorNombre(String textoNombre) {

        // Convertimos todo a minúscula para que no no sea case sensitive
        textoNombre = textoNombre.toLowerCase();

        // Creamos un nuevo arrayList para almacenar los empleados encontrados
        List<Empleado> encontrados = empleadoRepository.findByNombreContainingIgnoreCase(textoNombre);

        return encontrados;
    }

    // BÚSQUEDA POR GÉNERO
    public List<Empleado> buscarPorGenero(Genero genero) {
        List<Empleado> encontrados = empleadoRepository.findByGenero(genero);

        return encontrados;
    }

    // BÚSQUEDA POR DEPARTAMENTO
    public List<Empleado> buscarPorDepartamento(Long idDepto) {
        List<Empleado> encontrados = empleadoRepository.findByDepartamentoId(idDepto);

        return encontrados;
    }

}
