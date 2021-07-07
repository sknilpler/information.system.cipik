package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Сущность склад материальных ценностей (приход)
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bill;    //счет
    private String name; //наименование
    private double number; //количество
    private String unit; //единицы измерения
    private String price;//стоимость
    private String code; //код
    private String nomenclature; //номенклатурный номер

    @ManyToOne
    private Article article; //статья

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "item_id")
    private List<WriteOffAct> writeOffActs; //акты списания

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "item_id")
    private List<IssuanceItems> issuanceItems;  //выдано

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    @JoinColumn(name = "item_id")
    private List<Coming> comings;   //приход

    /**
     * Конструктор сущности материальной ценности на складе
     *
     * @param bill         счет
     * @param name         наименование
     * @param number       количество
     * @param unit         единицы измерения
     * @param code         код
     * @param nomenclature номенклатурный номер
     * @param article      статья
     */
    public Item(String bill, String name, double number, String unit, String code, String nomenclature, Article article, String price) {
        this.bill = bill;
        this.name = name;
        this.number = number;
        this.unit = unit;
        this.code = code;
        this.nomenclature = nomenclature;
        this.article = article;
        this.price = price;
    }
}
