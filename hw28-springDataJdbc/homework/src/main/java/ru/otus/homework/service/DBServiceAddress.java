package ru.otus.homework.service;

import ru.otus.homework.model.Address;
import ru.otus.homework.model.Client;

import java.util.List;
import java.util.Optional;

public interface DBServiceAddress {
    Address saveAddress(Address address);
}
