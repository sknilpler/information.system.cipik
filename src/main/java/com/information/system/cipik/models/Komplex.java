package com.information.system.cipik.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "komplex_id")
    private List<Otdel> otdels;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "otdel_id")
    private List<Employee> employees;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "komplex_id")
    @JsonIgnore
    private List<IssuedSIZ> issuedSIZS;


    public Komplex(String name, String adres, String shortName, Centr centr, Role role) {
        this.name = name;
        this.adres = adres;
        this.centr = centr;
        this.shortName = shortName;
        this.otdels = otdels;

        this.role = role;
    }



}
