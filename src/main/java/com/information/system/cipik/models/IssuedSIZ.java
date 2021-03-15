package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Сущность выданный СИЗ
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class IssuedSIZ {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Employee employee;

    @ManyToOne
    private Item item;

    //дата выдачи и окончания носки
    private Date dateIssued, dateEndWear;
    private String status;

    public IssuedSIZ(Employee employee, Item item, Date dateIssued, Date dateEndWear) {
        this.employee = employee;
        this.item = item;
        this.dateIssued = dateIssued;
        this.dateEndWear = dateEndWear;
    }
}
