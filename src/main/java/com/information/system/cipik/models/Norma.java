package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Norma {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Sredstvo sredstvo;

    @ManyToOne
    private Rashodniki rashodniki;

    private double number;

    public Norma(Sredstvo sredstvo, Rashodniki rashodniki, double number) {
        this.sredstvo = sredstvo;
        this.rashodniki = rashodniki;
        this.number = number;
    }
}
