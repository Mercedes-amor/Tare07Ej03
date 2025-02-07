package com.example.app.domain;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
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
@Entity // Declara que va a ser una entidad de la base de datos con nombre Empleado
public class Empleado {

    // @Id es obligatorio e indica que es la Primary Key de la entidad Empleado
    // Obligatoriamente es not null y único
    // Podríamos haberlo puesto a dni, matrícula, códigoCliente, etc
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Genera automáticamente el id autoincrementando
    private Long id;
    @NotNull
    private String nombre;
    @NotNull
    @Email
    private String email;
    @NotNull
    private Float salario;
    private Boolean enActivo;
    @NotNull
    private Genero genero;

    @ManyToOne // Relación uno a muchos
    // OnDelete()Para borrar en cascada, si se elimina el departamento
    // se borran todos los empleados que dependen de él
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Departamento departamento;

    // Relación uno a muchos Empleado - Nóminas
    // CascadeType.ALL implica -> orphanRemoval = true
    @ToString.Exclude  
    //OJO! tuve que cambiar EAGER POR LAZY para que le diera tiempo a ejecutarse,
    //ya que no se borraban las nóminas
    @OneToMany(fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL,
     mappedBy = "empleado") 
    private List<Nomina> nominas = new ArrayList<>();

    // También podría ser un HashSet<>,
    // que no tiene posiciones ni admite duplicados
    // private Set<Nomina> nominas = new HashSet<> ();

}
