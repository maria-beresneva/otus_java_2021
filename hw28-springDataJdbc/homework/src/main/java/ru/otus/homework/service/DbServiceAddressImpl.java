package ru.otus.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.homework.model.Address;
import ru.otus.homework.model.Client;
import ru.otus.homework.repository.AddressRepository;
import ru.otus.homework.repository.ClientRepository;
import ru.otus.homework.sessionmanager.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DbServiceAddressImpl implements DBServiceAddress {
    private static final Logger log = LoggerFactory.getLogger(DbServiceAddressImpl.class);

    private final TransactionManager transactionManager;
    private final AddressRepository addressRepository;

    public DbServiceAddressImpl(TransactionManager transactionManager, AddressRepository addressRepository) {
        this.transactionManager = transactionManager;
        this.addressRepository = addressRepository;
    }

    @Override
    public Address saveAddress(Address address) {
        return transactionManager.doInTransaction(() -> {
            var savedAddress = addressRepository.save(address);
            log.info("saved address: {}", savedAddress);
            return savedAddress;
        });
    }
}
