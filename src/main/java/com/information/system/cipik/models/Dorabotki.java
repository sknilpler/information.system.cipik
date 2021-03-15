package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Сущность доработки
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Dorabotki {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String naimen, osnovanie, ispolnitel;

    private LocalDate data_nach, data_kon;

    @ManyToOne
    private Stanciya stanciya;

    public Dorabotki(String naimen, String osnovanie, String ispolnitel, LocalDate data_nach, LocalDate data_kon, Stanciya stanciya) {
        this.naimen = naimen;
        this.osnovanie = osnovanie;
        this.ispolnitel = ispolnitel;
        this.data_nach = data_nach;
        this.data_kon = data_kon;
        this.stanciya = stanciya;
    }
}
