package com.information.system.cipik.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

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
@EqualsAndHashCode(exclude = {"issuedSIZS"})
@ToString(exclude = {"issuedSIZS"})
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String surname, name, patronymic;
    private String tabNomer;
    private Date dataStartWork;
    private String height, clothingSize, headgearSize, shoeSize, sex;

    private String gasMaskSize, respiratorSize, mittensSize, gloveSize;  // not used

    private String addres, telephone;  //not used
    private Date endWork, birthdayDate;  //not used

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "employee_id")
    @JsonIgnore
    private List<IssuedSIZ> issuedSIZS;

    @ManyToOne
    @JsonIgnore
    private Post post;

    @ManyToOne
    @JsonIgnore
    private Komplex komplex;

    public String getFIO(){
        return this.surname + " "+ this.name + " " + this.patronymic;
    }

    public String getShortFIO(){
        return this.surname + " "+ this.name.charAt(0) + " " + this.patronymic.charAt(0);
    }

    public Employee(String surname, String name, String patronymic, String sex, String tabNomer,
                    Post post, Komplex komplex) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.post = post;
        this.komplex = komplex;
        this.sex = sex;
        this.tabNomer = tabNomer;
    }

    public Employee(String surname, String name, String patronymic, String sex, String height, String clothingSize, String headgearSize, Post post, Komplex komplex) {
        this.surname = surname;
        this.name = name;
        this.patronymic = patronymic;
        this.sex = sex;
        this.height = height;
        this.clothingSize = clothingSize;
        this.headgearSize = headgearSize;
        this.post = post;
        this.komplex = komplex;
    }

    public Employee(String surname, String name, String patronymic, String tabNomer, String height,
                    String clothingSize, String headgearSize, String shoeSize, String sex, Post post, Komplex komplex) {
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
        this.komplex = komplex;
    }

    public Employee(String surname, String name, String patronymic, String tabNomer, Date dataStartWork, String height,
                    String clothingSize, String headgearSize, String shoeSize, String sex, Post post, Komplex komplex) {
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
        this.komplex = komplex;
    }



}
