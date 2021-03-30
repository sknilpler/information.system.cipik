package com.information.system.cipik.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private List<Employee> employees;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private IPMStandard ipmStandard;


}
