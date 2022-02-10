package ru.otus.homework.model;

import lombok.Getter;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Table("client")
public class Client {

    @Id
    private Long id;

    private String name;

    @MappedCollection(idColumn = "client_id")
    private Address address;

    @MappedCollection(idColumn = "client_id", keyColumn = "id")
    private List<Phone> phones;

    public Client(String name) {
        this.name = name;
    }

    @PersistenceConstructor
    public Client(Long id, String name, Address address, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phones = phones;
    }

    @Override
    public String toString() {
        return String.format("Client:\nid %d\nname %s\naddress %s\nphones %s",
                id,
                name,
                address,
                phones.stream()
                        .map(Objects::toString)
                        .collect(Collectors.joining(", ")));
    }
}
