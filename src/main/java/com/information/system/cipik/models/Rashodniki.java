package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Сущность расходник на ТО
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Rashodniki {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name, ed_izm;


    public Rashodniki(String name, String ed_izm) {
        this.name = name;
        this.ed_izm = ed_izm;
    }
}
