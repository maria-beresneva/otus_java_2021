package ru.otus.atm.storage;

public interface MoneyTray {

        int getFreeSpace();

        void withdraw(long banknotesNumber);

        void putSingleNote();

        int getBanknoteCount();
}
