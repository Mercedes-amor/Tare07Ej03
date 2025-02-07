package com.example.app.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.app.domain.Nomina;

@Repository
public interface NominaRepository extends JpaRepository <Nomina, Long>{

    //Método para verificar si ya existe una nómina con la misma fecha para el mismo empleado
    boolean existsByEmpleadoIdAndFechaNomina(Long empleadoId, LocalDate fechaNomina);

}
