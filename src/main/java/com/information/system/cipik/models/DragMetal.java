package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
public class DragMetal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private double number;

    @ManyToOne
    private Stanciya stanciya;

    public DragMetal(String name, double number, Stanciya stanciya) {
        this.name = name;
        this.number = number;
        this.stanciya = stanciya;
    }
}
