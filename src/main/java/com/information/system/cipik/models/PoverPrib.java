package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class PoverPrib {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String naimen, tip,zav_nom,chto_izm,diapazon,tochnost,period_prov;

    private LocalDate data_posl,data_plan,data_fakt;

    private String primech;

    @ManyToOne
    private Stanciya stanciya;

    public PoverPrib(String naimen, String tip, String zav_nom, String chto_izm, String diapazon, String tochnost,
                     String period_prov, LocalDate data_posl, LocalDate data_plan, LocalDate data_fakt, String primech,
                     Stanciya stanciya) {
        this.naimen = naimen;
        this.tip = tip;
        this.zav_nom = zav_nom;
        this.chto_izm = chto_izm;
        this.diapazon = diapazon;
        this.tochnost = tochnost;
        this.period_prov = period_prov;
        this.data_posl = data_posl;
        this.data_plan = data_plan;
        this.data_fakt = data_fakt;
        this.primech = primech;
        this.stanciya = stanciya;
    }
}
