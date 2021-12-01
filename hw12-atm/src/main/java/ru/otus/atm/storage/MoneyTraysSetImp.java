package ru.otus.atm.storage;

import ru.otus.atm.enums.AtmErrorType;
import ru.otus.atm.utils.AtmErrorException;

import java.util.ArrayList;
import java.util.List;

public class MoneyTraysSetImp implements MoneyTraysSet{
    private static final int DEFAULT_TRAY_SIZE = 50;
    private final List<MoneyTray> traySet;

    MoneyTraysSetImp() {
        traySet = new ArrayList<>();
    }

    @Override
    public void removeTray() {
        traySet.removeIf(tray -> tray.getBanknoteCount() == 0);
    }

    @Override
    public int getTotalNotesCount() {
        return traySet.stream().map(MoneyTray::getBanknoteCount).reduce(0, Integer::sum);
    }

    @Override
    public int getFreeSpace() {
        return traySet.stream().map(MoneyTray::getFreeSpace).reduce(0, Integer::sum);
    }

    @Override
    public void putSingleNote() {
        for (MoneyTray tray : traySet) {
            int availableSpace = tray.getFreeSpace();
            if (availableSpace > 0) {
                tray.putSingleNote();
                break;
            }
        }
    }

    @Override
    public void withdrawMoney(long banknotesToWithdraw) {
        if (banknotesToWithdraw <= 0)
            throw new AtmErrorException(
                    AtmErrorType.INCORRECT_INPUT,
                    String.format("Please withdraw only positive number of notes. You are trying to withdraw %d",
                            banknotesToWithdraw));
        int totalAvailableBills = getTotalNotesCount();
        if (banknotesToWithdraw > totalAvailableBills)
            throw new AtmErrorException(
                    AtmErrorType.INSUFFICIENT_ATM_FUNDS,
                    String.format("""
                                    Trying to withdraw more money then the trays hold!
                                    Attempting to withdraw: %d
                                    Available: %d""",
                    banknotesToWithdraw, totalAvailableBills));
        for (MoneyTray tray : traySet) {
            int availableBills = tray.getBanknoteCount();
            if (availableBills > 0) {
                long amountToWithdraw = Math.min(availableBills, banknotesToWithdraw);
                tray.withdraw(amountToWithdraw);
                banknotesToWithdraw -= amountToWithdraw;
            }
            if (banknotesToWithdraw == 0) break;
        }
    }

    @Override
    public void addTray(int countNotes) {
        traySet.add(new MoneyTrayImp(countNotes, DEFAULT_TRAY_SIZE));
    }

    @Override
    public int registeredTraysCount() {
        return traySet.size();
    }
}
