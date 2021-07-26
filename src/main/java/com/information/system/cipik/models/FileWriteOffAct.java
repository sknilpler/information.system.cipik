package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class FileWriteOffAct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;

    @Lob
    private byte[] data;


    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_write_off_act_id")
    private List<WriteOffAct> writeOffActs;

    public FileWriteOffAct(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }

}
