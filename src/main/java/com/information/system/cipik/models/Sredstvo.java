package com.information.system.cipik.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class Sredstvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name, indeks;

    public Sredstvo() {
    }

    public Sredstvo(String name, String indeks) {
        this.name = name;
        this.indeks = indeks;
    }
}
