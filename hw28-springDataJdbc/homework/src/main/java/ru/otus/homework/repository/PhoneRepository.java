package ru.otus.homework.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.homework.model.Address;
import ru.otus.homework.model.Phone;

public interface PhoneRepository extends CrudRepository<Phone, Long> {
}
