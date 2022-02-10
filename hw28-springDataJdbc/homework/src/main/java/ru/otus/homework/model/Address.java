package ru.otus.homework.model;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Table("address")
public class Address {

    @Id
    private Long id;

    private String street;

    private Long clientId;

    public Address(String street) {
        this.street = street;
    }

    @PersistenceConstructor
    public Address(Long id, String street, Long clientId) {
        this.id = id;
        this.street = street;
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return street;
    }
}
