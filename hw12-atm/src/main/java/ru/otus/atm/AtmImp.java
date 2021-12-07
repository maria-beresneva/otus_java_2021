package ru.otus.atm;

import ru.otus.atm.enums.AtmErrorType;
import ru.otus.atm.storage.MoneyStorage;
import ru.otus.atm.storage.MoneyStorageImp;
import ru.otus.atm.utils.AtmErrorException;
import ru.otus.atm.utils.AtmResponse;

public class AtmImp implements Atm{
    private final MoneyStorage moneyStorage;

    public AtmImp() {
        moneyStorage = new MoneyStorageImp();
    }

    public AtmResponse withdraw(int amount) {
        try {
            int total = moneyStorage.getTotalAmount();
            if (total < amount)
                throw new AtmErrorException(
                        AtmErrorType.INSUFFICIENT_ATM_FUNDS,
                        "Trying to withdraw more that atm have. Please change sum.");
            moneyStorage.withdrawMoney(amount);
            return new AtmResponse();
        } catch (AtmErrorException e) {
            return new AtmResponse(e);
        }
    }

    public AtmResponse insert(Banknote banknote) {
        registerTrayIfNecessary(banknote.getNominal());
        try {
            moneyStorage.insert(banknote);
            return new AtmResponse();
        } catch (AtmErrorException e) {
            return new AtmResponse(e);
        }
    }

    public AtmResponse registerTrayIfNecessary(int nominal) {
        try {
            if(moneyStorage.isRegisterTrayNecessary(nominal)){
                moneyStorage.registerNewEmptyTrayForNominal(nominal);
            }
            return new AtmResponse();
        } catch (AtmErrorException e) {
            return new AtmResponse(e);
        }
    }

    public void printOutAtmState() {
        moneyStorage.printOutMoneyStorageState();
    }

    public int getTotal() {
       return moneyStorage.getTotalAmount();
    }

    public int freeTraysCount() {
        return moneyStorage.freeTraysCount();
    }

    public int countTraysForNominal(int nominal) {
        return moneyStorage.countTraysForNominal(nominal);
    }

    public void registerAndFillTrayForNominal(int nominal, int countNotes) {
        moneyStorage.registerAndFillTrayForNominal(nominal, countNotes);
    }
}
