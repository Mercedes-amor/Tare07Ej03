package com.example.app;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.app.domain.Departamento;
import com.example.app.domain.Empleado;
import com.example.app.domain.Genero;
import com.example.app.services.DepartamentoService;
import com.example.app.services.EmpleadoService;

@SpringBootApplication
public class Main {

	@Autowired
	EmpleadoService empleadoService;

	public static void main(String[] args) {
		SpringApplication.run(Main.class, args);
		System.out.println("Tarea 7 Acceso a datos, Ej01");
	}

	@Bean
	CommandLineRunner initData(EmpleadoService empleadoService, DepartamentoService departamentoService) {
		return args -> {
			// Crear departamentos sin empleados inicialmente
			Departamento depto1 = new Departamento(null, "RRHH", 200000, new ArrayList<>());
			Departamento depto2 = new Departamento(null, "Administración", 200000, new ArrayList<>());
			Departamento depto3 = new Departamento(null, "Ventas", 200000, new ArrayList<>());

			// Guardar los departamentos en la base de datos
			departamentoService.add(depto1);
			departamentoService.add(depto2);
			departamentoService.add(depto3);

			// Crear empleados asociados a los departamentos
			Empleado emp1 = new Empleado(null, "pepe", "pepe@gmail.com", 25000f, true, Genero.MASCULINO, depto1);
			Empleado emp2 = new Empleado(null, "ana", "ana@gmail.com", 28000f, true, Genero.FEMENINO, depto2);
			Empleado emp3 = new Empleado(null, "Mercedes", "Mercedesamor@gmail.com", 30000f, true, Genero.FEMENINO,
					depto3);
			Empleado emp4 = new Empleado(null, "Indiana Jones", "laxsiempre@gmail.com", 28000f, false, Genero.OTROS,
					depto2);

			// Añadir empleados a los departamentos manualmente
			depto1.getEmpleados().add(emp1);
			depto2.getEmpleados().add(emp2);
			depto3.getEmpleados().add(emp3);
			depto2.getEmpleados().add(emp4);

			// Guardar empleados en la base de datos
			empleadoService.add(emp1);
			empleadoService.add(emp2);
			empleadoService.add(emp3);
			empleadoService.add(emp4);
		};
	}
}