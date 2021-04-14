package com.information.system.cipik.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

/**
 * Сущность должность
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"employees","ipmStandard"})
@ToString(exclude = {"employees","ipmStandard"})
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id")
    @JsonIgnore
    private List<IPMStandard> ipmStandard;


}
