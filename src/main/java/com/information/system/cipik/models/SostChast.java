package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class SostChast {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String naimen,zav_nom;

    @ManyToOne
    private Stanciya stanciya;

    public SostChast(String naimen, String zav_nom, Stanciya stanciya) {
        this.naimen = naimen;
        this.zav_nom = zav_nom;
        this.stanciya = stanciya;
    }
}
