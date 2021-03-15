package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Сущность комплекс
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Komplex {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name, adres;

    private String shortName;

    @ManyToOne
    private Centr centr;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "komplex_id")
    private List<Otdel> otdels;


    public Komplex(String name, String adres, String shortName, Centr centr, List<Otdel> otdels) {
        this.name = name;
        this.adres = adres;
        this.centr = centr;
        this.shortName = shortName;
        this.otdels = otdels;
    }

    public Komplex(String name, String adres, String shortName, Centr centr) {
        this.name = name;
        this.adres = adres;
        this.centr = centr;
        this.shortName = shortName;
    }


}
