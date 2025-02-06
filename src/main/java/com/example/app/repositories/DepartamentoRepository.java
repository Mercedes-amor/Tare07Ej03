package com.example.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.app.domain.Departamento;


//Empleado es la entidad que se almacenará en la base de datos y 
//Long es el tipo del identificador (@Id)
public interface DepartamentoRepository extends JpaRepository <Departamento, Long>{


}
