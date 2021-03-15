package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Сущность средство
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Sredstvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name, indeks;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sredstvo_id")
    private List<Norma> normas;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "sredstvo_id")
    private List<Stanciya> stanciyas;

    public Sredstvo(String name, String indeks) {
        this.name = name;
        this.indeks = indeks;
    }
}
