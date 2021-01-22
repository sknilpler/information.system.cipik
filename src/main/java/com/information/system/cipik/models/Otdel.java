package com.information.system.cipik.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
public class Otdel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    private Komplex komplex;

    public Otdel() {
    }

    public Otdel(String name) {
        this.name = name;
    }

    public Otdel(String name, Komplex komplex) {
        this.name = name;
        this.komplex = komplex;
    }
}
