package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Сущность акт списания материального средства
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class WriteOffAct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nameAct;
    private Date dateAct;
    private String komplex;
    private double numWriteOff;

    @ManyToOne
    private Item item;


    public WriteOffAct(Item item, String nameAct, Date dateAct, String komplex, double numWriteOff) {
        this.item = item;
        this.nameAct = nameAct;
        this.dateAct = dateAct;
        this.komplex = komplex;
        this.numWriteOff = numWriteOff;
    }
}
