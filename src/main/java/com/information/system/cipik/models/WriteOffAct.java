package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Сущность акт списания материального средства
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class WriteOffAct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameAct; //название акта
    private Date dateAct;   //дата акта
    private double numWriteOff; //количество списанного

    private String fileName; //имя файла
    private String fileType; //тип файла

    @Lob
    private byte[] file;  //скан акта

    @ManyToOne
    private Item item;  //МЦ со склада

    @ManyToOne
    private Komplex komplex;    //подразделение

    /**
     * Конструктор сущности Акт-списания
     *
     * @param nameAct     название акта
     * @param dateAct     дата акта
     * @param numWriteOff количество списанного
     * @param fileName    имя файла
     * @param fileType    тип файла
     * @param file        скан акта
     * @param item        МЦ со склада
     * @param komplex     подразделение
     */
    public WriteOffAct(String nameAct, Date dateAct, double numWriteOff, String fileName, String fileType, byte[] file, Item item, Komplex komplex) {
        this.nameAct = nameAct;
        this.dateAct = dateAct;
        this.numWriteOff = numWriteOff;
        this.fileName = fileName;
        this.fileType = fileType;
        this.file = file;
        this.item = item;
        this.komplex = komplex;
    }
}
