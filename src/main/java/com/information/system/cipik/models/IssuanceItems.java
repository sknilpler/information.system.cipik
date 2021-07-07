package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Сущность выданные материальные ценности со склада
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class IssuanceItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number; //количество
    private Date dateIssued; //дата выдачи
    private String invoiceNumber; //номер накладной
    private String employeeAccepting; //ФИО принявшего
    private String employeeGaveOut; //ФИО выдавшего
    @ManyToOne
    private Item item; //склад (МЦ)
    @ManyToOne
    private Komplex komplex; //подразделение

    /**
     * Конструктор сущности выдача МЦ со склада
     *
     * @param number            количество
     * @param dateIssued        дата выдачи
     * @param invoiceNumber     номер накладной
     * @param employeeAccepting ФИО принявшего
     * @param employeeGaveOut   ФИО выдавшего
     * @param item              материальная ценность
     * @param komplex           подразделение кому выдается
     */
    public IssuanceItems(String number, Date dateIssued, String invoiceNumber, String employeeAccepting, String employeeGaveOut, Item item, Komplex komplex) {
        this.number = number;
        this.dateIssued = dateIssued;
        this.invoiceNumber = invoiceNumber;
        this.employeeAccepting = employeeAccepting;
        this.employeeGaveOut = employeeGaveOut;
        this.item = item;
        this.komplex = komplex;
    }
}
