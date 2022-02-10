package ru.otus.homework.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.homework.model.Address;
import ru.otus.homework.model.Client;

public interface AddressRepository extends CrudRepository<Address, Long> {
}
