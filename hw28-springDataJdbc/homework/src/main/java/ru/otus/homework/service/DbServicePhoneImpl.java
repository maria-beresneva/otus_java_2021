package ru.otus.homework.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.homework.model.Address;
import ru.otus.homework.model.Phone;
import ru.otus.homework.repository.AddressRepository;
import ru.otus.homework.repository.PhoneRepository;
import ru.otus.homework.sessionmanager.TransactionManager;

import java.util.Optional;

@Service
public class DbServicePhoneImpl implements DBServicePhone {
    private static final Logger log = LoggerFactory.getLogger(DbServicePhoneImpl.class);

    private final TransactionManager transactionManager;
    private final PhoneRepository phoneRepository;

    public DbServicePhoneImpl(TransactionManager transactionManager, PhoneRepository phoneRepository) {
        this.transactionManager = transactionManager;
        this.phoneRepository = phoneRepository;
    }

    @Override
    public Phone savePhone(Phone address) {
        return transactionManager.doInTransaction(() -> {
            var savedPhone = phoneRepository.save(address);
            log.info("saved phone: {}", savedPhone);
            return savedPhone;
        });
    }
}
