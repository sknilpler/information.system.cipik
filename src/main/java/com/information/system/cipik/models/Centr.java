package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Centr {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name, shortName;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "centr_id")
    private List<Komplex> komplexes;

    public Centr(String name, String shortName) {
        this.name = name;
        this.shortName = shortName;
    }

}
