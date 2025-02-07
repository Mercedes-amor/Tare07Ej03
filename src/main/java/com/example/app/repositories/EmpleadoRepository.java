package com.example.app.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.app.domain.Departamento;
import com.example.app.domain.Empleado;
import com.example.app.domain.Genero;


//Empleado es la entidad que se almacenará en la base de datos y 
//Long es el tipo del identificador (@Id)
public interface EmpleadoRepository extends JpaRepository <Empleado, Long>{
   List<Empleado> findBySalario(Float salario); 
   List<Empleado> findBySalarioGreaterThanEqualOrderBySalario(Float salario); 
   List<Empleado> findBySalarioAndNombre(Float salario, String nombre); 
   List<Empleado> findByNombreContainingIgnoreCase(String nombre);
   List<Empleado> findByGenero(Genero genero);

   List<Empleado> findByDepartamento(Departamento departamento);
   List<Empleado> findByDepartamentoId(Long departamentoId);
 
   //Consulta que pide el Empleado que tenga el id más alto (el último)
   @Query ("select e from Empleado e where e.id=(select max(e2.id) from Empleado e2)")
    Empleado obtenerMaxIdEmpleado();

    @Query("SELECT e FROM Empleado e WHERE e.salario >= :salario ORDER BY e.salario")
    List<Empleado> findEmpleadosConSalarioMayorIgual(@Param("salario") Float salario);

    //Query para obtener la suma de los salarios de los empleados de un departamento
    @Query("SELECT COALESCE(SUM(e.salario), 0) FROM Empleado e WHERE e.departamento.id = :departamentoId")
    Float obtenerSumaSalarios(@Param("departamentoId") Long departamentoId);
}
