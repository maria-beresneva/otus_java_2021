package ru.otus.homework.repository;

import org.springframework.data.repository.CrudRepository;
import ru.otus.homework.model.Client;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
