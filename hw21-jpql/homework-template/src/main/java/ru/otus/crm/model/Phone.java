package ru.otus.crm.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "phone")
@Getter
@Setter
public class Phone implements Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private String number;

    public Phone() {
    }

    public Phone(String street) {
        this.id = null;
        this.number = street;
    }

    public Phone(Long id, String street) {
        this.id = id;
        this.number = street;
    }

    @Override
    public Phone clone() {
        return new Phone(this.id, this.number);
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", number='" + number + '\'' +
                '}';
    }
}
