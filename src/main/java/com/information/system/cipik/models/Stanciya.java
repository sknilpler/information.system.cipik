package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Stanciya {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String zav_nom, org_razr, org_izg;

    private LocalDate data_izg, data_vvoda;

    private String srok_sluzhb_snach, garant_srok,garant_res, srok_sluj;

    private String to_sv_s, to_stor_o;

    private String kompl, zip,ed,rab_tz,modern;

    private String primech;

    private double narabotka;

    private LocalDate data_pod_n,data_sled_n;

    private String fio_otv, doljn, mesto_rasp, uchet;

    @ManyToOne
    private Otdel otdel;

    @ManyToOne
    private Sredstvo sredstvo;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "stanciya_id")
    private List<SostChast> sostChasts;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "stanciya_id")
    private List<Rpr> rprs;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "stanciya_id")
    private List<DragMetal> dragMetals;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "stanciya_id")
    private List<PoverPrib> poverPribs;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "stanciya_id")
    private List<Dorabotki> dorabotkis;

    public Stanciya(String zav_nom, String org_razr, String org_izg, LocalDate data_izg, LocalDate data_vvoda,
                    String srok_sluzhb_snach, String garant_srok, String garant_res, String srok_sluj, String to_sv_s,
                    String to_stor_o, String kompl, String zip, String ed, String rab_tz, String modern, String primech,
                    double narabotka, LocalDate data_pod_n, LocalDate data_sled_n, String fio_otv, String doljn,
                    String mesto_rasp, String uchet, Otdel otdel, Sredstvo sredstvo) {
        this.zav_nom = zav_nom;
        this.org_razr = org_razr;
        this.org_izg = org_izg;
        this.data_izg = data_izg;
        this.data_vvoda = data_vvoda;
        this.srok_sluzhb_snach = srok_sluzhb_snach;
        this.garant_srok = garant_srok;
        this.garant_res = garant_res;
        this.srok_sluj = srok_sluj;
        this.to_sv_s = to_sv_s;
        this.to_stor_o = to_stor_o;
        this.kompl = kompl;
        this.zip = zip;
        this.ed = ed;
        this.rab_tz = rab_tz;
        this.modern = modern;
        this.primech = primech;
        this.narabotka = narabotka;
        this.data_pod_n = data_pod_n;
        this.data_sled_n = data_sled_n;
        this.fio_otv = fio_otv;
        this.doljn = doljn;
        this.mesto_rasp = mesto_rasp;
        this.uchet = uchet;
        this.otdel = otdel;
        this.sredstvo = sredstvo;
    }
}
