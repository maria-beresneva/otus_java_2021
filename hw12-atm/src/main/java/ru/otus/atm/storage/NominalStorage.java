package ru.otus.atm.storage;

import java.util.OptionalInt;

public interface NominalStorage {

    void removeTray(int nominal);

    int getTotalNotesCount(int nominal);

    int getTotalAmount();

    int getFreeSpace(int nominal);

    void putSingleNote(int nominal);

    void withdrawMoney(int nominal, long banknotesToWithdraw);

    int getAmountTraysForNominal(int nominal);

    Boolean canTrayBeUnregister(int nominal);

    void registerAnFillTrayForNominal(int nominal, int countNotes);

    void unregisterNominalTray(int nominal);

    void printOutMoneyStorageState();

    OptionalInt getMinimalNominalPresent();

    Boolean isBanknoteWithNominalCanBeDeposit(int nominal);

    int registeredTraysCount();
}

