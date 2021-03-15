package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


/**
 * Сущность сотрудник
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String surname, name, patronymic;

    private String height, clothingSize, headgearSize, shoeSize;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private List<IssuedSIZ> issuedSIZS;

    @ManyToOne
    private Post post;

    @ManyToOne
    private Otdel otdel;

    public Employee(String surname, String name, String patronymic, Post post, Otdel otdel) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.post = post;
        this.otdel = otdel;
    }

    public Employee(String surname, String name, String patronymic, String height, String clothingSize, String headgearSize, String shoeSize, Post post, Otdel otdel) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.height = height;
        this.clothingSize = clothingSize;
        this.headgearSize = headgearSize;
        this.shoeSize = shoeSize;
        this.post = post;
        this.otdel = otdel;
    }
}
