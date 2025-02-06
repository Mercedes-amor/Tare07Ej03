package com.example.app.domain;




import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
    private String nombre;
    
}
