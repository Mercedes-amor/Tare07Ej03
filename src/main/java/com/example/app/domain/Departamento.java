package com.example.app.domain;




import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor 
@EqualsAndHashCode(of = "id")
@Entity //Declara que va a ser una entidad de la base de datos con nombre Departamento
public class Departamento {

    //@Id es obligatorio e indica que es la Primary Key de la entidad Departamento
    //Obligatoriamente es not null y único
    //Podríamos haberlo puesto a dni, matrícula, códigoCliente, etc
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //Genera automáticamente el id autoincrementando
    private Long id;
    @NotNull
    @Column(unique = true) //evita duplicados a nivel base de datos
    private String nombre;
    @NotNull
    private float presupuestoAnual;

    @ToString.Exclude //Para evitar el bucle 
    //Ponemos MERGE en vez de ALL, para que persistan los empleados de un departamento cuando lo editamos
    @OneToMany(cascade =  CascadeType.MERGE, mappedBy = "departamento", orphanRemoval = true)
    private List<Empleado> empleados = new ArrayList<>();
}
