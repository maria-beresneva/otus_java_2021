package ru.otus.homework.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table("phone")
public class Phone {

    @Id
    private Long id;

    private String number;

    private Long clientId;

    public Phone(String number) {
        this.number = number;
    }

    @PersistenceConstructor
    public Phone(Long id, String number, Long clientId) {
        this.id = id;
        this.number = number;
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return number;
    }
}
