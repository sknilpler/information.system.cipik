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

    private String nameAct; //название акта
    private Date dateAct;   //дата акта
    private double numWriteOff; //количество списанного

    @ManyToOne
    private Item item;  //МЦ со склада

    @ManyToOne
    private Komplex komplex;    //подразделение

    @ManyToOne
    private FileWriteOffAct fileWriteOffAct;

    /**
     * Конструктор сущности Акт-списания
     *
     * @param nameAct     название акта
     * @param dateAct     дата акта
     * @param numWriteOff количество списанного
     * @param item        МЦ со склада
     * @param komplex     подразделение
     * @param fileWriteOffAct файл скана акта
     */
    public WriteOffAct(String nameAct, Date dateAct, double numWriteOff, Item item, Komplex komplex, FileWriteOffAct fileWriteOffAct) {
        this.nameAct = nameAct;
        this.dateAct = dateAct;
        this.numWriteOff = numWriteOff;
        this.item = item;
        this.komplex = komplex;
        this.fileWriteOffAct = fileWriteOffAct;
    }
}
