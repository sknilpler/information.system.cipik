package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * Сущность протокол продления
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class ProtocolExtending {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String protocolName;
    private Date protocolDate;
    private Date dateExtending;

    @ManyToOne
    private IssuedSIZ issuedSIZ;

    public ProtocolExtending(String protocolName, Date protocolDate, Date dateExtending) {
        this.protocolName = protocolName;
        this.protocolDate = protocolDate;
        this.dateExtending = dateExtending;
    }

    public ProtocolExtending(String protocolName, Date protocolDate, Date dateExtending, IssuedSIZ issuedSIZ) {
        this.protocolName = protocolName;
        this.protocolDate = protocolDate;
        this.dateExtending = dateExtending;
        this.issuedSIZ = issuedSIZ;
    }

    @Override
    public String toString() {
        return "ProtocolExtending{" +
                "id=" + id +
                ", protocolName='" + protocolName + '\'' +
                ", protocolDate=" + protocolDate +
                ", dateExtending=" + dateExtending +
                ", issuedSIZ ID=" + issuedSIZ.getId() +
                '}';
    }
}
