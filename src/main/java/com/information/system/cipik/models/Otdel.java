package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Сущность отдел
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Otdel {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    private Komplex komplex;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "otdel_id")
    private List<Stanciya> stanciyas;


    public Otdel(String name, Komplex komplex) {
        this.name = name;
        this.komplex = komplex;
    }
}
