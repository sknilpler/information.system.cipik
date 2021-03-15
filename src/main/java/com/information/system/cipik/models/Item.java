package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Сущность склад
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bill;
    private String expense_item;
    private String name;
    private String number;
    private String price;
    private String unit; //Еденицы измерения
    private String code;
    private int count;
    private int dlc_count;
    private int all_count;
    private boolean issued;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    private List<IssuedSIZ> issuedSIZS;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    private List<WriteOffAct> writeOffActs;

    public Item(String bill, String expense_item, String name, String number, String price, String unit, String code, int count) {
        this.bill = bill;
        this.expense_item = expense_item;
        this.name = name;
        this.number = number;
        this.price = price;
        this.unit = unit;
        this.code = code;
        this.count = count;
        this.issued = false;
    }


}
