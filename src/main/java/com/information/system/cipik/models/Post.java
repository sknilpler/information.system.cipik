package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * Сущность должность
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String postName;

    public Post(String postName) {
        this.postName = postName;
    }

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private List<Employee> employees;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    private IPMStandard ipmStandard;


}
