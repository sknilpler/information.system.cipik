package com.information.system.cipik.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class Centr {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "centr_id")
    private List<Komplex> komplexes;

    public Centr() {
    }

    public Centr(String name) {
        this.name = name;
    }

    public Centr(String name, List<Komplex> komplexes) {
        this.name = name;
        this.komplexes = komplexes;
    }

}
