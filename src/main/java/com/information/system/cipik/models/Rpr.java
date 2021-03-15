package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Сущность РПР
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Rpr {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nom_uved,nom_son;

    private LocalDate data_son;

    private String neispr_v, neispr_h,ustr_neispr,zakl_kom,nom_ai;

    private LocalDate data_ai;

    private String nom_aur;

    private LocalDate data_aur;

    private String primechanie;

    private boolean udovlet, zakrita;

    @ManyToOne
    private Stanciya stanciya;

    public Rpr(String nom_uved, String nom_son, LocalDate data_son, String neispr_v, String neispr_h,
               String ustr_neispr, String zakl_kom, String nom_ai, LocalDate data_ai, String nom_aur,
               LocalDate data_aur, String primechanie, boolean udovlet, boolean zakrita, Stanciya stanciya) {
        this.nom_uved = nom_uved;
        this.nom_son = nom_son;
        this.data_son = data_son;
        this.neispr_v = neispr_v;
        this.neispr_h = neispr_h;
        this.ustr_neispr = ustr_neispr;
        this.zakl_kom = zakl_kom;
        this.nom_ai = nom_ai;
        this.data_ai = data_ai;
        this.nom_aur = nom_aur;
        this.data_aur = data_aur;
        this.primechanie = primechanie;
        this.udovlet = udovlet;
        this.zakrita = zakrita;
        this.stanciya = stanciya;
    }
}
