package ru.otus.atm.storage;

import ru.otus.atm.enums.AtmErrorType;
import ru.otus.atm.enums.Nominal;
import ru.otus.atm.utils.AtmErrorException;

import java.util.*;

public class NominalStorageImp implements NominalStorage {
    private final Map<Integer, MoneyTraysSet> nominalTraySet;

    NominalStorageImp() {
        nominalTraySet = new HashMap<>();
    }

    @Override
    public void removeTray(int nominal) {
        nominalTraySet.get(nominal).removeTray();
    }

    @Override
    public int getTotalNotesCount(int nominal) {
        return nominalTraySet.get(nominal).getTotalNotesCount();
    }

    @Override
    public int getTotalAmount() {
        return nominalTraySet.keySet().stream()
            .mapToInt(moneyTrays -> getTotalNotesCount(moneyTrays) * moneyTrays)
            .sum();
    }

    @Override
    public int getFreeSpace(int nominal) {
        return nominalTraySet.get(nominal).getFreeSpace();
    }

    @Override
    public void putSingleNote(int nominal) {
        nominalTraySet.get(nominal).putSingleNote();
    }

    @Override
    public void withdrawMoney(int nominal, long banknotesToWithdraw) {
        nominalTraySet.get(nominal).withdrawMoney(banknotesToWithdraw);
    }

    @Override
    public int getAmountTraysForNominal(int nominal) {
        return (nominalTraySet.get(nominal) != null) ? nominalTraySet.get(nominal).registeredTraysCount() : 0;
    }

    @Override
    public Boolean canTrayBeUnregister(int nominal) {
        return ((getTotalNotesCount(nominal) % nominalTraySet.get(nominal).registeredTraysCount()) == 0);
    }

    @Override
    public void registerAnFillTrayForNominal(int nominal, int countNotes) {
        if (!Nominal.checkNominalSupported(nominal))
            throw new AtmErrorException(AtmErrorType.INCORRECT_INPUT, String.format("Nominal %s is not supported", nominal));
        if(nominalTraySet.get(nominal) == null) {
            nominalTraySet.put(nominal, new MoneyTraysSetImp());
        }
        nominalTraySet.get(nominal).addTray(countNotes);
    }

    @Override
    public void unregisterNominalTray(int nominal) {
        if (!Nominal.checkNominalSupported(nominal))
            throw new AtmErrorException(AtmErrorType.INCORRECT_INPUT, String.format("Nominal %s is not supported", nominal));
        if(canTrayBeUnregister(nominal)) {
            removeTray(nominal);
            if(getAmountTraysForNominal(nominal) == 0) {
                nominalTraySet.remove(nominal);
            }
        }
    }

    @Override
    public void printOutMoneyStorageState() {
        nominalTraySet.forEach((nominal, traySet) -> {
            if (getTotalNotesCount(nominal) > 0)
                System.out.printf("Nominal %s, balance: %d, notes count: %d\n%n",
                        nominal,
                        getTotalNotesCount(nominal) * nominal,
                        getTotalNotesCount(nominal));
        });
    }

    @Override
    public OptionalInt getMinimalNominalPresent() {
        return nominalTraySet.keySet().stream()
                .filter(moneyTrays -> getTotalNotesCount(moneyTrays) > 0)
                .mapToInt(i -> i)
                .reduce(Integer::min);
    }

    @Override
    public Boolean isBanknoteWithNominalCanBeDeposit(int nominal) {
        var nominalStorage = nominalTraySet.get(nominal);
        return (nominalStorage != null) && (getFreeSpace(nominal) > 0);
    }

    @Override
    public int registeredTraysCount() {
        return nominalTraySet.values().stream().mapToInt(MoneyTraysSet::registeredTraysCount).sum();
    }
}

