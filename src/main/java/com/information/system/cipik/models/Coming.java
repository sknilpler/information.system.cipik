package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Сущность Приход МЦ на склад
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Coming {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bill;        //счет
    private String name;        //наименование
    private String number;      //количество
    private String price;       //стоимость за единицу
    private String unit;        //единицы измерения
    private String nomenclature;        //номенклатурный номер
    private Date dateOfReceive; //дата прихода

    @ManyToOne
    private Item item; //склад (МЦ)

    /**
     * Конструктор объекта сущность приход
     *
     * @param name          наименование
     * @param number        количество
     * @param price         стоимость за единицу
     * @param unit          единицы измерения
     * @param nomenclature          номенклатурный номер
     * @param dateOfReceive дата прихода
     */
    public Coming(String name,String bill, String number, String price, String unit, String nomenclature, Date dateOfReceive, Item item) {
        this.name = name;
        this.bill = bill;
        this.number = number;
        this.price = price;
        this.unit = unit;
        this.nomenclature = nomenclature;
        this.dateOfReceive = dateOfReceive;
        this.item= item;
    }
}
