package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Komplex {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name, adres;

    @ManyToOne
    private Centr centr;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "komplex_id")
    private List<Otdel> otdels;


    public Komplex(String name, String adres, Centr centr, List<Otdel> otdels) {
        this.name = name;
        this.adres = adres;
        this.centr = centr;
        this.otdels = otdels;
    }

    public Komplex(String name, String adres, List<Otdel> otdels) {
        this.name = name;
        this.adres = adres;
        this.otdels = otdels;
    }

    public Komplex(String name, String adres) {
        this.name = name;
        this.adres = adres;
    }

}
