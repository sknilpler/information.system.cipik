package com.information.system.cipik.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;


/**
 * Сущность история авторизаций
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
public class HistoryAuthorization {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private Date authorizationTime;

    public HistoryAuthorization(String username, Date authorizationTime) {
        this.username = username;
        this.authorizationTime = authorizationTime;
    }
}
