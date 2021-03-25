package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
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
    private String tabNomer;
    private Date dataStartWork;
    private String height, clothingSize, headgearSize, shoeSize, sex;

    private String gasMaskSize, respiratorSize,mittensSize,gloveSize;  // not used

    private String addres, telephone;  //not used
    private Date endWork, birthdayDate;  //not used

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    private List<IssuedSIZ> issuedSIZS;

    @ManyToOne
    private Post post;

    @ManyToOne
    private Otdel otdel;

    public Employee(String surname, String name, String patronymic, String sex, String tabNomer,
                    Post post, Otdel otdel) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.post = post;
        this.otdel = otdel;
        this.sex = sex;
        this.tabNomer = tabNomer;
    }


    public Employee(String surname, String name, String patronymic, String tabNomer, String height,
                    String clothingSize, String headgearSize, String shoeSize, String sex, Post post, Otdel otdel) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.tabNomer = tabNomer;
        this.height = height;
        this.clothingSize = clothingSize;
        this.headgearSize = headgearSize;
        this.shoeSize = shoeSize;
        this.sex = sex;
        this.post = post;
        this.otdel = otdel;
    }

    public Employee(String surname, String name, String patronymic, String tabNomer, Date dataStartWork, String height,
                    String clothingSize, String headgearSize, String shoeSize, String sex, Post post, Otdel otdel) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.tabNomer = tabNomer;
        this.dataStartWork = dataStartWork;
        this.height = height;
        this.clothingSize = clothingSize;
        this.headgearSize = headgearSize;
        this.shoeSize = shoeSize;
        this.sex = sex;
        this.post = post;
        this.otdel = otdel;
    }
}
