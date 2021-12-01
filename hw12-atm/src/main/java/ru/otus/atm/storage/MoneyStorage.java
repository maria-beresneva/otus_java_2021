package ru.otus.atm.storage;

import ru.otus.atm.Banknote;

public interface MoneyStorage {

    void insert(Banknote banknote);

    int getTotalAmount();

    void printOutMoneyStorageState();

    void withdrawMoney(double amountToWithdraw);

    void registerNewEmptyTrayForNominal(int nominal);

    void registerAndFillTrayForNominal(int nominal, int countNotes);

    int freeTraysCount();

    int countTraysForNominal (int nominal);

    Boolean isRegisterTrayNecessary(int nominal);
}
