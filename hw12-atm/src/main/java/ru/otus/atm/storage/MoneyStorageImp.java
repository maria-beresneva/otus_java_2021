package ru.otus.atm.storage;

import ru.otus.atm.Banknote;
import ru.otus.atm.enums.AtmErrorType;
import ru.otus.atm.enums.Nominal;
import ru.otus.atm.utils.AtmErrorException;

import java.util.HashMap;
import java.util.Map;
import java.util.OptionalInt;

public class MoneyStorageImp implements MoneyStorage{
    private static final int DEFAULT_SET_TRAY_SIZE = 5;
    private static final int INIT_VALUE = 0;
    NominalStorage nominalStorage;

    public MoneyStorageImp() {
        nominalStorage = new NominalStorageImp();
    }

    @Override
    public void insert(Banknote banknote) {
        if (!Nominal.checkNominalSupported(banknote.getNominal()))
            throw new AtmErrorException(
                    AtmErrorType.INCORRECT_INPUT,
                    String.format("Nominal %s is not supported", banknote.getNominal()));
        if(!isBanknoteWithNominalCanBeDeposit(banknote.getNominal()))
            throw new AtmErrorException(
                    AtmErrorType.INSUFFICIENT_STORAGE,
                    "Amount of trays reach the limit");
        nominalStorage.putSingleNote(banknote.getNominal());
    }

    @Override
    public int getTotalAmount() {
        return nominalStorage.getTotalAmount();
    }

    @Override
    public void printOutMoneyStorageState() {
        nominalStorage.printOutMoneyStorageState();
    }

    private OptionalInt getMinimalNominalPresent() {
        return nominalStorage.getMinimalNominalPresent();
    }

    private Map<Integer, Long> breakAmountIntoNominals(Double amountToBreak) {
        Map<Integer, Long> nominalAmountsToWithdraw = new HashMap<>();
        for (Nominal nominal : Nominal.values()) {
            long notesShouldBeWithdrawn = amountToBreak.longValue() / nominal.getNominal();
            int availableNotes = nominalStorage.getTotalNotesCount(nominal.getNominal());
            long notesToWithdraw = Math.min(notesShouldBeWithdrawn, availableNotes);
            if (notesToWithdraw > 0) {
                amountToBreak -= notesToWithdraw * nominal.getNominal();
                nominalAmountsToWithdraw.put(nominal.getNominal(), notesToWithdraw);
            }
            if (amountToBreak == 0.0) return nominalAmountsToWithdraw;
        }
        throw new AtmErrorException(AtmErrorType.INSUFFICIENT_ATM_FUNDS,
                String.format("""
                                Failed to break requested amount into available banknotes!
                                Leftover amount: %f
                                in banknotes: %s""",
                amountToBreak, nominalAmountsToWithdraw));
    }

    @Override
    public void withdrawMoney(double amountToWithdraw) {
        if (amountToWithdraw <= 0)
            throw new AtmErrorException(
                    AtmErrorType.INCORRECT_INPUT,
                    String.format("Please withdraw only positive amount. You are trying to withdraw %f", amountToWithdraw));

        if (amountToWithdraw > getTotalAmount())
            throw new AtmErrorException(
                    AtmErrorType.INSUFFICIENT_ATM_FUNDS,
                    String.format("""
                                    Trying to withdraw more money then the ATM has!
                                    Attempting to withdraw: %f
                                    Available: %d""",
                    amountToWithdraw, getTotalAmount()));

        var minimalNoteNominal = getMinimalNominalPresent().getAsInt();
        if (amountToWithdraw % minimalNoteNominal > 0)
            throw new AtmErrorException(AtmErrorType.INCORRECT_INPUT, String.format("Amount must divide by %d without leftover. " +
                    "Please specify another sum. You are trying to withdraw %f", minimalNoteNominal, amountToWithdraw));

        Map<Integer, Long> nominalAmountsToWithdraw = breakAmountIntoNominals(amountToWithdraw);
        nominalAmountsToWithdraw.forEach((nominal, numberOfNotes) -> {
            nominalStorage.withdrawMoney(nominal, numberOfNotes);
            nominalStorage.unregisterNominalTray(nominal);
        });
    }

    @Override
    public void registerNewEmptyTrayForNominal(int nominal) {
        registerAndFillTrayForNominal(nominal, INIT_VALUE);
    }

    @Override
    public void registerAndFillTrayForNominal(int nominal, int countNotes) {
        if(!canAnotherTrayBeRegister())
            throw new AtmErrorException(AtmErrorType.INSUFFICIENT_STORAGE, "Amount of trays reach the limit");
        nominalStorage.registerAnFillTrayForNominal(nominal, countNotes);
    }

    private Boolean isBanknoteWithNominalCanBeDeposit(int nominal) {
       return nominalStorage.isBanknoteWithNominalCanBeDeposit(nominal);
    }

    @Override
    public int freeTraysCount() {
        int busyCapacity = nominalStorage.registeredTraysCount();
        return DEFAULT_SET_TRAY_SIZE - busyCapacity;
    }

    @Override
    public int countTraysForNominal (int nominal) {
       return nominalStorage.getAmountTraysForNominal(nominal);
    }

    @Override
    public Boolean isRegisterTrayNecessary(int nominal) {
        return !isBanknoteWithNominalCanBeDeposit(nominal);
    }

    private Boolean canAnotherTrayBeRegister() {
        return (nominalStorage.registeredTraysCount() < DEFAULT_SET_TRAY_SIZE);
    }
}
