package ru.otus.atm;

import ru.otus.atm.utils.AtmResponse;

public interface Atm {

    AtmResponse withdraw(int amount);

    AtmResponse insert(Banknote banknote);

    AtmResponse registerTrayIfNecessary(int nominal);

    void printOutAtmState();

    int getTotal();

    int freeTraysCount();

    int countTraysForNominal(int nominal);

    void registerAndFillTrayForNominal(int nominal, int countNotes);
}
